package com.nexus.gamehub.core
import android.content.Context
import androidx.compose.runtime.*
object AppSettings {
    val palette = listOf(0xFF00E5FF, 0xFFFF2E88, 0xFF9DFF3B, 0xFFFFB300, 0xFFB388FF).map { it.toInt() }
    var accent by mutableIntStateOf(palette[0]); var crossColor by mutableIntStateOf(0xFFFF0000.toInt())
    var crossSize by mutableIntStateOf(96); var hudPos by mutableIntStateOf(0)
    fun load(c: Context) { val s = c.getSharedPreferences("settings", 0)
        accent = s.getInt("accent", accent); crossColor = s.getInt("cc", crossColor); crossSize = s.getInt("cs", 96).coerceIn(48, 160); hudPos = s.getInt("hp", 0).coerceIn(0, 3) }
    fun save(c: Context) { c.getSharedPreferences("settings", 0).edit().putInt("accent", accent).putInt("cc", crossColor).putInt("cs", crossSize).putInt("hp", hudPos).apply() }
}
