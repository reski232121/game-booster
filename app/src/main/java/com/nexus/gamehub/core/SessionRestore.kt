package com.nexus.gamehub.core
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.provider.Settings
object SessionRestore {
    private fun sp(c: Context) = c.getSharedPreferences("restore", 0)
    fun snapshot(c: Context) {
        restore(c)
        val cr = c.contentResolver; val nm = c.getSystemService(NotificationManager::class.java)
        sp(c).edit().putBoolean("pending", true)
            .putInt("br", Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS, 128))
            .putInt("brm", Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL))
            .putBoolean("sync", ContentResolver.getMasterSyncAutomatically())
            .putInt("dnd", nm.currentInterruptionFilter).apply()
    }
    fun restoreBrightness(c: Context) {
        val s = sp(c); if (!Settings.System.canWrite(c) || !s.contains("br")) return
        runCatching {
            Settings.System.putInt(c.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, s.getInt("brm", 0))
            Settings.System.putInt(c.contentResolver, Settings.System.SCREEN_BRIGHTNESS, s.getInt("br", 128))
        }
    }
    fun restoreIfPending(c: Context) = restore(c)
    fun restore(c: Context) {
        val s = sp(c); if (!s.getBoolean("pending", false)) return
        restoreBrightness(c)
        runCatching { ContentResolver.setMasterSyncAutomatically(s.getBoolean("sync", true)) }
        val nm = c.getSystemService(NotificationManager::class.java); val d = s.getInt("dnd", 1)
        if (nm.isNotificationPolicyAccessGranted && d in 1..4) nm.setInterruptionFilter(d)
        s.edit().putBoolean("pending", false).apply()
    }
}
