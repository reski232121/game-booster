package com.nexus.gamehub.feature.device
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.core.SystemStats
import com.nexus.gamehub.ui.components.*
@Composable fun DeviceScreen() {
    val ram = SystemStats.ram(LocalContext.current)
    val rows = listOf("Perangkat" to "${Build.MANUFACTURER} ${Build.MODEL}", "Android" to "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
        "Chipset" to Build.HARDWARE, "CPU Core" to Runtime.getRuntime().availableProcessors().toString(), "ABI" to Build.SUPPORTED_ABIS.first(), "RAM Total" to "${ram.totalMb} MB")
    Column(Modifier.padding(16.dp)) {
        Title("Info Perangkat")
        NeonCard { rows.forEach { (k, v) -> Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), Arrangement.SpaceBetween) { Text(k, color = Color.Gray); Text(v) } } }
    }
}
