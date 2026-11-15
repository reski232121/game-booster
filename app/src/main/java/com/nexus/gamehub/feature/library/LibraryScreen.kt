package com.nexus.gamehub.feature.library
import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.nexus.gamehub.feature.boostplay.BoostPlay
import com.nexus.gamehub.feature.favorites.FavoriteStore
import com.nexus.gamehub.feature.shortcuts.ShortcutSync
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
private data class App(val name: String, val pkg: String, val game: Boolean, val icon: ImageBitmap)
@Composable fun LibraryScreen() {
    val ctx = LocalContext.current; val scope = rememberCoroutineScope()
    var apps by remember { mutableStateOf(listOf<App>()) }
    var onlyGames by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    var favs by remember { mutableStateOf(FavoriteStore.get(ctx)) }
    var msg by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        apps = withContext(Dispatchers.IO) {
            val pm = ctx.packageManager
            pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0).map {
                val ai = it.activityInfo.applicationInfo
                @Suppress("DEPRECATION") val g = ai.category == ApplicationInfo.CATEGORY_GAME || (ai.flags and ApplicationInfo.FLAG_IS_GAME) != 0
                App(it.loadLabel(pm).toString(), ai.packageName, g, it.loadIcon(pm).toBitmap(96, 96).asImageBitmap())
            }.filter { it.pkg != ctx.packageName }
        }
    }
    LaunchedEffect(apps, favs) { ShortcutSync.sync(ctx, favs.mapNotNull { f -> apps.firstOrNull { it.pkg == f } }.map { Triple(it.pkg, it.name, it.icon.asAndroidBitmap()) }) }
    val shown = apps.filter { (!onlyGames || it.game || it.pkg in favs) && it.name.contains(query, true) }
        .sortedWith(compareByDescending<App> { it.pkg in favs }.thenBy { it.name.lowercase() })
    Column(Modifier.padding(16.dp)) {
        Title("Game Library")
        OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text("Cari game") }, singleLine = true)
        Row(verticalAlignment = Alignment.CenterVertically) { Text("Hanya game"); Spacer(Modifier.width(8.dp)); Switch(onlyGames, { onlyGames = it }) }
        if (msg.isNotEmpty()) Text(msg, color = Lime)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 12.dp)) {
            items(shown, key = { it.pkg }) { a ->
                NeonCard(Modifier.clickable { ctx.packageManager.getLaunchIntentForPackage(a.pkg)?.let(ctx::startActivity) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(a.icon, null, Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)))
                        Spacer(Modifier.width(12.dp)); Text(a.name, Modifier.weight(1f))
                        IconButton({ favs = FavoriteStore.toggle(ctx, a.pkg) }) { Icon(Icons.Filled.Star, "Favorit", tint = if (a.pkg in favs) Neon else Color.Gray) }
                        IconButton({ scope.launch { msg = "Menyiapkan ${a.name}..."; msg = BoostPlay.run(ctx, a.pkg) } }) { Icon(Icons.Filled.PlayArrow, "Boost & Play", tint = Lime) }
                    }
                }
            }
        }
    }
}
