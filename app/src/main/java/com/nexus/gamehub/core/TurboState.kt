package com.nexus.gamehub.core
import androidx.compose.runtime.*
object TurboState {
    var active by mutableStateOf(false); var pkg by mutableStateOf<String?>(null)
    var ping by mutableIntStateOf(-1); var heat by mutableIntStateOf(0); var cleaned by mutableLongStateOf(0L)
    fun begin(p: String?) { active = true; pkg = p; ping = -1; heat = 0; cleaned = 0 }
    fun end() { active = false; ping = -1; heat = 0 }
}
