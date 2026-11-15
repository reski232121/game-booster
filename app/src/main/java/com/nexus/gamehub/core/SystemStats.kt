package com.nexus.gamehub.core
import android.app.ActivityManager
import android.content.*
import android.os.BatteryManager
data class Ram(val usedMb: Long, val totalMb: Long, val availMb: Long) { val pct get() = if (totalMb == 0L) 0 else (usedMb * 100 / totalMb).toInt() }
data class Bat(val pct: Int, val tempC: Float, val charging: Boolean)
object SystemStats {
    fun ram(c: Context): Ram {
        val mi = ActivityManager.MemoryInfo()
        (c.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(mi)
        val t = mi.totalMem / 1048576; val a = mi.availMem / 1048576
        return Ram(t - a, t, a)
    }
    fun battery(c: Context): Bat {
        val i = c.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val l = i?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
        val s = i?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
        val t = (i?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0) / 10f
        val st = i?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return Bat(l * 100 / s, t, st == BatteryManager.BATTERY_STATUS_CHARGING || st == BatteryManager.BATTERY_STATUS_FULL)
    }
}
