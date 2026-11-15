package com.nexus.gamehub.feature.crosshair
import android.content.Context
import android.graphics.*
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.nexus.gamehub.core.AppSettings
import com.nexus.gamehub.core.OverlayWindow
class CrosshairOverlay(private val c: Context) {
    private val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var v: View? = null
    fun show(shape: Int) {
        remove()
        val size = (AppSettings.crossSize * c.resources.displayMetrics.density).toInt()
        val col = AppSettings.crossColor
        val view = object : View(c) {
            override fun onDraw(cv: Canvas) {
                val w = width.toFloat(); val x = w / 2; val y = height / 2f
                val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = col; strokeWidth = w * 0.04f }
                when (shape) {
                    0 -> { p.style = Paint.Style.FILL; cv.drawCircle(x, y, w * 0.06f, p) }
                    1 -> { cv.drawLine(x - w * 0.29f, y, x + w * 0.29f, y, p); cv.drawLine(x, y - w * 0.29f, x, y + w * 0.29f, p) }
                    else -> { p.style = Paint.Style.STROKE; cv.drawCircle(x, y, w * 0.25f, p); p.style = Paint.Style.FILL; cv.drawCircle(x, y, w * 0.03f, p) }
                }
            }
        }
        v = view; wm.addView(view, OverlayWindow.params(size, size, Gravity.CENTER))
    }
    fun remove() { v?.let { runCatching { wm.removeView(it) } }; v = null }
}
