package com.nexus.gamehub.feature.overlay
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.core.OverlayState
import com.nexus.gamehub.core.OverlayWindow
import com.nexus.gamehub.ui.components.*
import kotlinx.coroutines.delay
@Composable fun OverlayScreen() {
    val ctx = LocalContext.current
    var granted by remember { mutableStateOf(OverlayWindow.canDraw(ctx)) }
    LaunchedEffect(Unit) { while (true) { granted = OverlayWindow.canDraw(ctx); delay(1000) } }
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (!granted) NeonCard {
            Text("Izin 'Tampil di atas aplikasi lain' dibutuhkan untuk overlay."); Spacer(Modifier.height(8.dp))
            Button(onClick = { ctx.startActivity(OverlayWindow.permissionIntent(ctx)) }) { Text("Beri izin") }
        }
        NeonCard {
            Row(verticalAlignment = Alignment.CenterVertically) { Text("Crosshair", Modifier.weight(1f))
                Switch(OverlayState.crosshair, { OverlayState.crosshair = it; OverlayService.sync(ctx) }, enabled = granted) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Titik", "Plus", "Lingkaran").forEachIndexed { i, n ->
                    if (OverlayState.shape == i) Button({ }) { Text(n) } else OutlinedButton({ OverlayState.shape = i; if (OverlayState.crosshair) OverlayService.sync(ctx) }) { Text(n) }
                }
            }
        }
        NeonCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("HUD melayang"); Text("Refresh layar, RAM, suhu, ping (bukan FPS game)", style = MaterialTheme.typography.bodySmall) }
                Switch(OverlayState.hud, { OverlayState.hud = it; OverlayService.sync(ctx) }, enabled = granted)
            }
        }
    }
}
