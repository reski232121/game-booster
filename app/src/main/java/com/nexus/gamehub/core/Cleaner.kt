package com.nexus.gamehub.core
import android.app.ActivityManager
import android.content.Context
import kotlinx.coroutines.delay
object Cleaner {
    suspend fun clean(c: Context, keep: String? = null) {
        val am = c.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        c.packageManager.getInstalledApplications(0).filter { it.packageName != c.packageName && it.packageName != keep }.forEach { am.killBackgroundProcesses(it.packageName) }
        delay(800)
    }
}
