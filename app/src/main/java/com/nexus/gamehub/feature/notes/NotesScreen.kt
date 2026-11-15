package com.nexus.gamehub.feature.notes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.ui.components.*
import com.nexus.gamehub.ui.theme.*
private fun clean(s: String) = s.replace("\n", " ").replace("\t", " ").trim()
@Composable fun NotesScreen() {
    val ctx = LocalContext.current
    val sp = remember { ctx.getSharedPreferences("notes", 0) }
    var items by remember { mutableStateOf((sp.getString("items", "") ?: "").lines().filter { it.isNotBlank() }) }
    var game by remember { mutableStateOf("") }; var note by remember { mutableStateOf("") }
    fun save(l: List<String>) { items = l; sp.edit().putString("items", l.joinToString("\n")).apply() }
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(game, { game = it }, Modifier.fillMaxWidth(), label = { Text("Nama game") }, singleLine = true)
        OutlinedTextField(note, { note = it }, Modifier.fillMaxWidth(), label = { Text("Sensitivitas / setting / catatan") })
        Button({ if (game.isNotBlank()) { save(items + (clean(game) + "\t" + clean(note))); game = ""; note = "" } }) { Text("Simpan") }
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(items) { i, s ->
                val p = s.split("\t", limit = 2)
                NeonCard { Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) { Text(p[0], color = Neon); Text(p.getOrElse(1) { "" }) }
                    TextButton({ save(items.filterIndexed { j, _ -> j != i }) }) { Text("Hapus", color = Pink) }
                } }
            }
        }
    }
}
