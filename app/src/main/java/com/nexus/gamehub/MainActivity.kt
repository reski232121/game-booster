package com.nexus.gamehub
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nexus.gamehub.feature.booster.BoosterScreen
import com.nexus.gamehub.feature.tools.ToolsScreen
import com.nexus.gamehub.feature.library.LibraryScreen
import com.nexus.gamehub.feature.monitor.MonitorScreen
import com.nexus.gamehub.feature.ping.PingScreen
import com.nexus.gamehub.ui.theme.*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.nexus.gamehub.core.AppSettings.load(this)
        setContent { GameHubTheme {
            var tab by rememberSaveable { mutableIntStateOf(0) }
            val items = listOf("Library" to Icons.Filled.Home, "Boost" to Icons.Filled.Star, "Monitor" to Icons.Filled.Info, "Ping" to Icons.Filled.Refresh, "Tools" to Icons.Filled.Build)
            Scaffold(containerColor = Bg, bottomBar = {
                NavigationBar(containerColor = Card) { items.forEachIndexed { i, (n, ic) ->
                    NavigationBarItem(selected = tab == i, onClick = { tab = i }, icon = { Icon(ic, n) }, label = { Text(n) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Bg, indicatorColor = Neon)) } }
            }) { pad -> Box(Modifier.padding(pad)) { when (tab) { 0 -> LibraryScreen(); 1 -> BoosterScreen(); 2 -> MonitorScreen(); 3 -> PingScreen(); else -> ToolsScreen() } } }
        } }
    }
}
