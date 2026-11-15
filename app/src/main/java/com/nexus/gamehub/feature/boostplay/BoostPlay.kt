package com.nexus.gamehub.feature.boostplay
import android.content.Context
import android.content.Intent
import com.nexus.gamehub.core.Cleaner
import com.nexus.gamehub.core.SystemStats
import com.nexus.gamehub.feature.turbo.TurboService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
object BoostPlay {
    suspend fun run(c: Context, pkg: String): String {
        val i = c.packageManager.getLaunchIntentForPackage(pkg) ?: return "Game tidak ditemukan"
        val before = SystemStats.ram(c).availMb
        withContext(Dispatchers.Default) { Cleaner.clean(c, pkg) }
        val freed = (SystemStats.ram(c).availMb - before).coerceAtLeast(0)
        TurboService.start(c, pkg)
        c.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return "Turbo aktif • Boost $freed MB"
    }
}
