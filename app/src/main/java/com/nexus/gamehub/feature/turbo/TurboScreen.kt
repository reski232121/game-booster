package com.nexus.gamehub.feature.turbo
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.core.TurboState
import com.nexus.gamehub.feature.advisor.GraphicsAdvisor
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
import kotlinx.coroutines.delay
@Composable fun TurboScreen() {
    val ctx = LocalContext.current
    var canWrite by remember { mutableStateOf(Settings.System.canWrite(ctx)) }
    LaunchedEffect(Unit) { while (true) { canWrite = Settings.System.canWrite(ctx); delay(1000) } }
    val advice = remember { GraphicsAdvisor.advise(ctx) }
    Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NeonCard {
            Text(if (TurboState.active) "TURBO AKTIF" else "Turbo nonaktif", color = if (TurboState.active) Lime else Color.Gray, style = MaterialTheme.typography.titleLarge)
            if (TurboState.active) {
                TurboState.pkg?.let { Text("Game: $it") }
                Text("Ping: " + if (TurboState.ping < 0) "--" else "${TurboState.ping} ms")
                Text("Suhu: " + listOf("Aman", "Hangat", "Panas")[TurboState.heat.coerceIn(0, 2)])
                Text("Memory Guard membebaskan ${TurboState.cleaned} MB")
            }
            Spacer(Modifier.height(8.dp))
            Button({ if (TurboState.active) TurboService.stop(ctx) else TurboService.start(ctx, null) }, Modifier.fillMaxWidth()) { Text(if (TurboState.active) "STOP TURBO" else "MULAI TURBO") }
        }
        if (!canWrite) NeonCard {
            Text("Heat Guard butuh izin 'Ubah pengaturan sistem' agar bisa menurunkan kecerahan saat HP panas."); Spacer(Modifier.height(8.dp))
            Button({ ctx.startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:${ctx.packageName}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }) { Text("Beri izin") }
        }
        NeonCard { Text("Graphics Advisor: ${advice.tier}", color = Neon); advice.tips.forEach { Text("• $it") } }
    }
}
