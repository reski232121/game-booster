package com.nexus.gamehub.feature.turbo
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.nexus.gamehub.core.*
import com.nexus.gamehub.feature.heatguard.HeatGuard
import com.nexus.gamehub.feature.memoryguard.MemoryGuard
import com.nexus.gamehub.feature.networkguard.NetworkGuard
import kotlinx.coroutines.*
class TurboService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var wake: PowerManager.WakeLock? = null
    private var net: NetworkGuard? = null
    override fun onBind(i: Intent?): IBinder? = null
    override fun onStartCommand(i: Intent?, f: Int, id: Int): Int {
        if (i?.action == ACTION_STOP) { stopSelf(); return START_NOT_STICKY }
        startAsForeground()
        if (TurboState.active) return START_NOT_STICKY
        val pkg = i?.getStringExtra("pkg")
        TurboState.begin(pkg)
        SessionRestore.snapshot(this)
        FocusMode.set(this, true)
        wake = getSystemService(PowerManager::class.java).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "nexus:turbo").also { it.acquire(4 * 60 * 60 * 1000L) }
        val n = NetworkGuard(this).also { it.enable() }; net = n
        val heat = HeatGuard(this)
        scope.launch {
            var tick = 0
            while (isActive) {
                TurboState.heat = heat.tick(SystemStats.battery(this@TurboService).tempC)
                if (tick % 2 == 0) TurboState.ping = n.ping()
                if (tick % 6 == 0) TurboState.cleaned += MemoryGuard.check(this@TurboService, pkg)
                tick++; delay(5000)
            }
        }
        return START_NOT_STICKY
    }
    override fun onDestroy() {
        scope.cancel(); wake?.let { if (it.isHeld) it.release() }; net?.disable()
        SessionRestore.restore(this); TurboState.end(); super.onDestroy()
    }
    private fun startAsForeground() {
        getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel("turbo", "Turbo Session", NotificationManager.IMPORTANCE_LOW))
        val stop = PendingIntent.getService(this, 0, Intent(this, TurboService::class.java).setAction(ACTION_STOP), PendingIntent.FLAG_IMMUTABLE)
        @Suppress("DEPRECATION") val n = Notification.Builder(this, "turbo").setSmallIcon(android.R.drawable.ic_menu_manage).setContentTitle("Turbo Session aktif")
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stop).build()
        if (Build.VERSION.SDK_INT >= 34) startForeground(2, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE) else startForeground(2, n)
    }
    companion object {
        private const val ACTION_STOP = "com.nexus.gamehub.STOP_TURBO"
        fun start(c: Context, pkg: String?) { c.startForegroundService(Intent(c, TurboService::class.java).putExtra("pkg", pkg)) }
        fun stop(c: Context) { c.stopService(Intent(c, TurboService::class.java)) }
    }
}
