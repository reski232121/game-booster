package com.nexus.gamehub.feature.boostplay
import android.content.Context
import android.content.Intent
import com.nexus.gamehub.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
object BoostPlay {
    suspend fun run(c: Context, pkg: String): String {
        val i = c.packageManager.getLaunchIntentForPackage(pkg) ?: return "Game tidak ditemukan"
        val before = SystemStats.ram(c).availMb
        withContext(Dispatchers.Default) { Cleaner.clean(c, pkg) }
        val freed = (SystemStats.ram(c).availMb - before).coerceAtLeast(0)
        val focus = FocusMode.set(c, true)
        c.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return "Boost $freed MB" + if (focus) " • Mode Fokus aktif" else " • (izin Mode Fokus belum diberikan)"
    }
}
