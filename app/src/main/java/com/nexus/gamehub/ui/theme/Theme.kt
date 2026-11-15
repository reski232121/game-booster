package com.nexus.gamehub.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nexus.gamehub.core.AppSettings
val Neon: Color get() = Color(AppSettings.accent)
val Pink = Color(0xFFFF2E88); val Bg = Color(0xFF0B0E17); val Card = Color(0xFF151A2A); val Lime = Color(0xFF9DFF3B)
@Composable fun GameHubTheme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = darkColorScheme(primary = Neon, secondary = Pink, background = Bg, surface = Card, onBackground = Color.White, onSurface = Color.White),
    content = content)
