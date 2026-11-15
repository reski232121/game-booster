package com.nexus.gamehub.feature.overlay
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.nexus.gamehub.core.OverlayState
import com.nexus.gamehub.core.OverlayWindow
import com.nexus.gamehub.feature.crosshair.CrosshairOverlay
import com.nexus.gamehub.feature.hud.HudOverlay
class OverlayService : Service() {
    private var cross: CrosshairOverlay? = null
    private var hud: HudOverlay? = null
    override fun onBind(i: Intent?): IBinder? = null
    override fun onStartCommand(i: Intent?, f: Int, id: Int): Int {
        startAsForeground()
        if (!OverlayWindow.canDraw(this)) { stopSelf(); return START_NOT_STICKY }
        if (OverlayState.crosshair) { val c = cross ?: CrosshairOverlay(this).also { cross = it }; c.show(OverlayState.shape) } else { cross?.remove(); cross = null }
        if (OverlayState.hud) { val h = hud ?: HudOverlay(this).also { hud = it }; h.show() } else { hud?.remove(); hud = null }
        if (cross == null && hud == null) stopSelf()
        return START_NOT_STICKY
    }
    override fun onDestroy() { cross?.remove(); hud?.remove(); super.onDestroy() }
    private fun startAsForeground() {
        getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel("overlay", "Overlay", NotificationManager.IMPORTANCE_LOW))
        val n = Notification.Builder(this, "overlay").setSmallIcon(android.R.drawable.ic_menu_view).setContentTitle("Nexus overlay aktif").build()
        if (Build.VERSION.SDK_INT >= 34) startForeground(1, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE) else startForeground(1, n)
    }
    companion object {
        fun sync(c: Context) { val i = Intent(c, OverlayService::class.java); if (OverlayState.crosshair || OverlayState.hud) c.startForegroundService(i) else c.stopService(i) }
    }
}
