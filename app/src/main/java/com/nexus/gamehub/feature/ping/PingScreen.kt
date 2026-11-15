package com.nexus.gamehub.feature.ping
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.net.Socket
private suspend fun probe(host: String): List<Long> = withContext(Dispatchers.IO) {
    List(5) {
        try { val t = System.nanoTime(); Socket().use { s -> s.connect(InetSocketAddress(host, 53), 2000) }; (System.nanoTime() - t) / 1_000_000 } catch (e: Exception) { -1L }
    }
}
@Composable fun PingScreen() {
    val scope = rememberCoroutineScope()
    val hosts = listOf("Google DNS" to "8.8.8.8", "Cloudflare" to "1.1.1.1")
    var res by remember { mutableStateOf(mapOf<String, List<Long>>()) }
    var busy by remember { mutableStateOf(false) }
    Column(Modifier.padding(16.dp)) {
        Title("Ping & Jaringan")
        Button(enabled = !busy, modifier = Modifier.fillMaxWidth(), onClick = {
            busy = true; scope.launch { res = hosts.associate { it.first to probe(it.second) }; busy = false }
        }) { Text(if (busy) "Mengukur..." else "TES PING") }
        Spacer(Modifier.height(12.dp))
        res.forEach { (n, l) ->
            val ok = l.filter { it >= 0 }; val avg = if (ok.isEmpty()) -1 else ok.average().toInt()
            val jit = if (ok.size > 1) ok.max() - ok.min() else 0L
            NeonCard(Modifier.padding(bottom = 10.dp)) {
                Text(n, color = Neon)
                StatBar("Ping rata-rata", if (avg < 0) 100 else avg / 2, if (avg < 0) "Gagal" else "$avg ms", if (avg in 0..60) Lime else Pink)
                Text("Jitter $jit ms • Loss ${(l.size - ok.size) * 100 / l.size}%")
                Text(when { avg < 0 -> "Tidak ada koneksi"; avg <= 50 -> "Sangat baik untuk game online"; avg <= 100 -> "Cukup baik"; else -> "Lag terasa, ganti jaringan" })
            }
        }
    }
}
