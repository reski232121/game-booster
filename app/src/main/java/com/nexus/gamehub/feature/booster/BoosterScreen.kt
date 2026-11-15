package com.nexus.gamehub.feature.booster
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.core.SystemStats
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
import kotlinx.coroutines.*
@Composable fun BoosterScreen() {
    val ctx = LocalContext.current; val scope = rememberCoroutineScope()
    var ram by remember { mutableStateOf(SystemStats.ram(ctx)) }
    var msg by remember { mutableStateOf("Siap boost") }
    var busy by remember { mutableStateOf(false) }
    var dnd by remember { mutableStateOf(false) }
    Column(Modifier.padding(16.dp)) {
        Title("Game Booster")
        NeonCard {
            StatBar("RAM terpakai", ram.pct, "${ram.usedMb}/${ram.totalMb} MB", Pink)
            Text(msg, color = Lime); Spacer(Modifier.height(12.dp))
            Button(enabled = !busy, onClick = {
                busy = true
                scope.launch {
                    val before = SystemStats.ram(ctx).availMb
                    withContext(Dispatchers.Default) {
                        com.nexus.gamehub.core.Cleaner.clean(ctx)
                    }
                    ram = SystemStats.ram(ctx)
                    msg = "Bebas ${(ram.availMb - before).coerceAtLeast(0)} MB RAM"; busy = false
                }
            }, modifier = Modifier.fillMaxWidth()) { Text(if (busy) "Memproses..." else "BOOST SEKARANG") }
        }
        Spacer(Modifier.height(12.dp))
        NeonCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("Mode Fokus (Jangan Ganggu)"); Text("Blokir notifikasi saat main", color = Color.Gray) }
                Switch(dnd, {
                    val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    if (!nm.isNotificationPolicyAccessGranted) ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    else { nm.setInterruptionFilter(if (it) NotificationManager.INTERRUPTION_FILTER_NONE else NotificationManager.INTERRUPTION_FILTER_ALL); dnd = it }
                })
            }
        }
    }
}
