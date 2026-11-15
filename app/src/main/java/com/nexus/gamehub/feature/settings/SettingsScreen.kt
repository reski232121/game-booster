package com.nexus.gamehub.feature.settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.core.AppSettings
import com.nexus.gamehub.feature.overlay.OverlayService
import com.nexus.gamehub.ui.components.*
@Composable private fun Colors(sel: Int, pick: (Int) -> Unit) = Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(vertical = 8.dp)) {
    AppSettings.palette.forEach { c ->
        Box(Modifier.size(36.dp).background(Color(c), CircleShape).then(if (c == sel) Modifier.border(3.dp, Color.White, CircleShape) else Modifier).clickable { pick(c) })
    }
}
@Composable fun SettingsScreen() {
    val ctx = LocalContext.current
    fun apply() { AppSettings.save(ctx); OverlayService.sync(ctx) }
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NeonCard { Text("Warna aksen"); Colors(AppSettings.accent) { AppSettings.accent = it; AppSettings.save(ctx) } }
        NeonCard {
            Text("Warna crosshair"); Colors(AppSettings.crossColor) { AppSettings.crossColor = it; apply() }
            Text("Ukuran crosshair: ${AppSettings.crossSize} dp")
            Slider(AppSettings.crossSize.toFloat(), { AppSettings.crossSize = it.toInt() }, valueRange = 48f..160f, onValueChangeFinished = { apply() })
        }
        NeonCard {
            Text("Posisi HUD")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("↖", "↗", "↙", "↘").forEachIndexed { i, n ->
                    if (AppSettings.hudPos == i) Button({ }) { Text(n) } else OutlinedButton({ AppSettings.hudPos = i; apply() }) { Text(n) }
                }
            }
        }
    }
}
