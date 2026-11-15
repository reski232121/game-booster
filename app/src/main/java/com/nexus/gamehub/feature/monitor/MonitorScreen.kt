package com.nexus.gamehub.feature.monitor
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.core.SystemStats
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
import kotlinx.coroutines.delay
@Composable fun MonitorScreen() {
    val ctx = LocalContext.current
    var ram by remember { mutableStateOf(SystemStats.ram(ctx)) }
    var bat by remember { mutableStateOf(SystemStats.battery(ctx)) }
    LaunchedEffect(Unit) { while (true) { ram = SystemStats.ram(ctx); bat = SystemStats.battery(ctx); delay(1000) } }
    Column(Modifier.padding(16.dp)) {
        Title("Live Monitor")
        NeonCard {
            StatBar("RAM", ram.pct, "${ram.pct}%", Neon)
            StatBar("Baterai${if (bat.charging) " (charging)" else ""}", bat.pct, "${bat.pct}%", Lime)
            StatBar("Suhu", (bat.tempC * 2).toInt(), "${bat.tempC} °C", if (bat.tempC > 40) Pink else Neon)
            if (bat.tempC > 42) Text("Perangkat panas! Istirahatkan sebentar.", color = Pink, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
