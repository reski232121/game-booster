package com.nexus.gamehub.feature.advisor
import android.content.Context
import android.os.Build
import com.nexus.gamehub.core.SystemStats
data class Advice(val tier: String, val tips: List<String>)
object GraphicsAdvisor {
    fun advise(c: Context): Advice {
        val ram = SystemStats.ram(c).totalMb; val cores = Runtime.getRuntime().availableProcessors()
        val extra = if (Build.VERSION.SDK_INT <= 28) listOf("Android versi lama: tutup aplikasi lain dan jangan multitasking saat main") else emptyList()
        return when {
            ram < 3000 || cores <= 4 -> Advice("HP kelas Entry (RAM ${ram}MB)", listOf("Grafis: Smooth / Rendah", "Batas FPS: Rendah-Medium", "Matikan bayangan, anti-aliasing, dan efek partikel", "Turunkan resolusi/skala render bila tersedia") + extra)
            ram < 6000 -> Advice("HP kelas Menengah (RAM ${ram}MB)", listOf("Grafis: Seimbang", "Batas FPS: Medium-Tinggi", "Matikan bayangan bila HP cepat panas") + extra)
            else -> Advice("HP kelas Atas (RAM ${ram}MB)", listOf("Grafis: HD/Tinggi", "Batas FPS: Tinggi/Maksimum", "Pantau suhu, turunkan grafis jika HUD merah") + extra)
        }
    }
}
