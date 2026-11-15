package com.nexus.gamehub.feature.timer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
import kotlinx.coroutines.delay
private fun fmt(s: Long) = "%02d:%02d:%02d".format(s / 3600, s / 60 % 60, s % 60)
@Composable fun TimerScreen() {
    val sp = remember { LocalContext.current.getSharedPreferences("timer", 0) }
    var start by remember { mutableLongStateOf(sp.getLong("start", 0)) }
    var acc by remember { mutableLongStateOf(sp.getLong("acc", 0)) }
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) { while (true) { now = System.currentTimeMillis(); delay(1000) } }
    val session = if (start > 0) (now - start) / 1000 else 0L
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NeonCard {
            Text("Sesi ini", color = Neon); Text(fmt(session), style = MaterialTheme.typography.displaySmall)
            Text("Total bermain: ${fmt(acc + session)}")
            if (acc + session >= 3600) Text("Sudah lebih dari 1 jam, saatnya istirahat!", color = Pink)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button({
                if (start == 0L) { start = System.currentTimeMillis(); sp.edit().putLong("start", start).apply() }
                else { acc += session; start = 0; sp.edit().putLong("start", 0).putLong("acc", acc).apply() }
            }) { Text(if (start == 0L) "Mulai" else "Stop") }
            OutlinedButton({ start = 0; acc = 0; sp.edit().clear().apply() }) { Text("Reset") }
        }
    }
}
