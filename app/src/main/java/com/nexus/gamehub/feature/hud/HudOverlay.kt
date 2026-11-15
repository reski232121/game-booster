package com.nexus.gamehub.feature.hud
import android.content.Context
import android.graphics.Color
import android.view.Choreographer
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.nexus.gamehub.core.AppSettings
import com.nexus.gamehub.core.OverlayWindow
import com.nexus.gamehub.core.SystemStats
class HudOverlay(private val c: Context) : Choreographer.FrameCallback {
    private val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var tv: TextView? = null
    private var frames = 0; private var last = 0L
    fun show() {
        tv?.let { wm.updateViewLayout(it, OverlayWindow.params(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, gravity())); return }
        val t = TextView(c).apply { setTextColor(Color.GREEN); textSize = 12f; setBackgroundColor(0x99000000.toInt()); setPadding(16, 8, 16, 8) }
        tv = t; wm.addView(t, OverlayWindow.params(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, gravity()))
        last = System.nanoTime(); Choreographer.getInstance().postFrameCallback(this)
    }
    override fun doFrame(frameTimeNanos: Long) {
        val t = tv ?: return
        frames++
        if (frameTimeNanos - last >= 1_000_000_000L) {
            t.text = "Display ${frames}Hz | RAM ${SystemStats.ram(c).pct}% | ${SystemStats.battery(c).tempC}°C"
            frames = 0; last = frameTimeNanos
        }
        Choreographer.getInstance().postFrameCallback(this)
    }
    private fun gravity() = listOf(Gravity.TOP or Gravity.START, Gravity.TOP or Gravity.END, Gravity.BOTTOM or Gravity.START, Gravity.BOTTOM or Gravity.END)[AppSettings.hudPos]
    fun remove() { Choreographer.getInstance().removeFrameCallback(this); tv?.let { runCatching { wm.removeView(it) } }; tv = null }
}
