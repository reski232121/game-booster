package com.nexus.gamehub.feature.saver
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.core.SystemStats
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
import kotlinx.coroutines.delay
@Composable fun SaverScreen() {
    val ctx = LocalContext.current
    var bat by remember { mutableStateOf(SystemStats.battery(ctx)) }
    LaunchedEffect(Unit) { while (true) { bat = SystemStats.battery(ctx); delay(2000) } }
    val tips = buildList {
        if (bat.tempC >= 42) add("Suhu tinggi (${bat.tempC}°C): lepas casing, turunkan grafis, dan hentikan pengisian saat bermain.")
        else if (bat.tempC >= 38) add("Suhu mulai hangat: kurangi kecerahan layar dan tutup aplikasi latar.")
        if (bat.pct <= 20 && !bat.charging) add("Baterai ${bat.pct}%: aktifkan Mode Hemat Baterai atau colok charger.")
        if (isEmpty()) add("Kondisi aman, silakan main!")
    }
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NeonCard { StatBar("Baterai", bat.pct, "${bat.pct}%", Lime); StatBar("Suhu", (bat.tempC * 2).toInt(), "${bat.tempC} °C", if (bat.tempC >= 40) Pink else Neon) }
        tips.forEach { NeonCard { Text(it) } }
        Button({ runCatching { ctx.startActivity(Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) } }, Modifier.fillMaxWidth()) { Text("Buka pengaturan Hemat Baterai") }
    }
}
