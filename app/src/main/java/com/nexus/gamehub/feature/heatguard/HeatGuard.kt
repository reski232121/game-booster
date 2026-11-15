package com.nexus.gamehub.feature.heatguard
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import com.nexus.gamehub.core.SessionRestore
class HeatGuard(private val c: Context) {
    private var steps = 0; private var lastVib = 0L
    fun tick(tempC: Float): Int {
        if (tempC >= 43f) { vibrate(); dim() } else if (tempC < 40f && steps > 0) { SessionRestore.restoreBrightness(c); steps = 0 }
        return if (tempC >= 43f) 2 else if (tempC >= 40f) 1 else 0
    }
    private fun vibrate() {
        val now = System.currentTimeMillis(); if (now - lastVib < 60_000) return; lastVib = now
        c.getSystemService(Vibrator::class.java)?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
    }
    private fun dim() {
        if (steps >= 3 || !Settings.System.canWrite(c)) return
        runCatching {
            val cr = c.contentResolver; val cur = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS, 128)
            Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
            Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS, (cur * 0.8f).toInt().coerceAtLeast(40)); steps++
        }
    }
}
