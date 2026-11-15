package com.nexus.gamehub.feature.memoryguard
import android.content.Context
import com.nexus.gamehub.core.Cleaner
import com.nexus.gamehub.core.SystemStats
object MemoryGuard {
    suspend fun check(c: Context, keep: String?): Long {
        val r = SystemStats.ram(c)
        if (r.totalMb == 0L || r.availMb * 100 / r.totalMb >= 20) return 0
        Cleaner.clean(c, keep)
        return (SystemStats.ram(c).availMb - r.availMb).coerceAtLeast(0)
    }
}
