package com.nexus.gamehub.feature.hud
import android.content.Context
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.os.Handler
import android.os.Looper
import android.view.Display
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.nexus.gamehub.core.*
class HudOverlay(private val c: Context) {
    private val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val h = Handler(Looper.getMainLooper())
    private var tv: TextView? = null
    private val tick = object : Runnable { override fun run() { val t = tv ?: return; update(t); h.postDelayed(this, 1000) } }
    private fun gravity() = listOf(Gravity.TOP or Gravity.START, Gravity.TOP or Gravity.END, Gravity.BOTTOM or Gravity.START, Gravity.BOTTOM or Gravity.END)[AppSettings.hudPos]
    private fun params() = OverlayWindow.params(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, gravity())
    fun show() {
        tv?.let { wm.updateViewLayout(it, params()); return }
        val t = TextView(c).apply { textSize = 12f; setBackgroundColor(0x99000000.toInt()); setPadding(16, 8, 16, 8) }
        tv = t; wm.addView(t, params()); update(t); h.postDelayed(tick, 1000)
    }
    private fun update(t: TextView) {
        val hz = c.getSystemService(DisplayManager::class.java).getDisplay(Display.DEFAULT_DISPLAY)?.refreshRate?.toInt() ?: 0
        val ping = TurboState.ping
        t.text = "${hz}Hz | RAM ${SystemStats.ram(c).pct}% | ${SystemStats.battery(c).tempC}°C | ping " + if (ping < 0) "--" else "${ping}ms"
        t.setTextColor(if (TurboState.heat == 2 || ping > 100) Color.RED else Color.GREEN)
    }
    fun remove() { h.removeCallbacks(tick); tv?.let { runCatching { wm.removeView(it) } }; tv = null }
}
