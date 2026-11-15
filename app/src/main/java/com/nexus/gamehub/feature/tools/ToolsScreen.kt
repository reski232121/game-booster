package com.nexus.gamehub.feature.tools
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.nexus.gamehub.feature.device.DeviceScreen
import com.nexus.gamehub.feature.notes.NotesScreen
import com.nexus.gamehub.feature.overlay.OverlayScreen
import com.nexus.gamehub.feature.timer.TimerScreen
import com.nexus.gamehub.feature.saver.SaverScreen
import com.nexus.gamehub.feature.settings.SettingsScreen
import com.nexus.gamehub.ui.components.Title
import com.nexus.gamehub.ui.theme.*
@Composable fun ToolsScreen() {
    var t by rememberSaveable { mutableIntStateOf(0) }
    Column {
        Column(Modifier.padding(start = 16.dp, top = 16.dp)) { Title("Tools") }
        ScrollableTabRow(selectedTabIndex = t, containerColor = Bg, contentColor = Neon, edgePadding = 8.dp) {
            listOf("Overlay", "Timer", "Catatan", "Hemat", "Setelan", "Device").forEachIndexed { i, n -> Tab(selected = t == i, onClick = { t = i }, text = { Text(n) }) }
        }
        when (t) { 0 -> OverlayScreen(); 1 -> TimerScreen(); 2 -> NotesScreen(); 3 -> SaverScreen(); 4 -> SettingsScreen(); else -> DeviceScreen() }
    }
}
