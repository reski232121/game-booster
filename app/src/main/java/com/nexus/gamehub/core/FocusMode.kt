package com.nexus.gamehub.core
import android.app.NotificationManager
import android.content.Context
object FocusMode {
    fun set(c: Context, on: Boolean): Boolean {
        val nm = c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!nm.isNotificationPolicyAccessGranted) return false
        nm.setInterruptionFilter(if (on) NotificationManager.INTERRUPTION_FILTER_NONE else NotificationManager.INTERRUPTION_FILTER_ALL); return true
    }
}
