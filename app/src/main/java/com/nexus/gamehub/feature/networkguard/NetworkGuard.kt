package com.nexus.gamehub.feature.networkguard
import android.content.ContentResolver
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
class NetworkGuard(private val c: Context) {
    private var lock: WifiManager.WifiLock? = null
    fun enable() {
        val wm = c.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        @Suppress("DEPRECATION") val mode = if (Build.VERSION.SDK_INT >= 29) WifiManager.WIFI_MODE_FULL_LOW_LATENCY else WifiManager.WIFI_MODE_FULL_HIGH_PERF
        lock = wm.createWifiLock(mode, "nexus:turbo").also { it.acquire() }
        runCatching { ContentResolver.setMasterSyncAutomatically(false) }
    }
    fun disable() { lock?.let { if (it.isHeld) it.release() }; lock = null }
    suspend fun ping(): Int = withContext(Dispatchers.IO) {
        try { val t = System.nanoTime(); Socket().use { it.connect(InetSocketAddress("8.8.8.8", 53), 1500) }; ((System.nanoTime() - t) / 1_000_000).toInt() } catch (e: Exception) { -1 }
    }
}
