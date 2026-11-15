package com.nexus.gamehub.feature.shortcuts
import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.drawable.Icon
object ShortcutSync {
    fun sync(c: Context, items: List<Triple<String, String, Bitmap>>) {
        val sm = c.getSystemService(ShortcutManager::class.java) ?: return
        val list = items.take(minOf(3, sm.maxShortcutCountPerActivity)).mapNotNull { (pkg, name, bmp) ->
            val i = c.packageManager.getLaunchIntentForPackage(pkg) ?: return@mapNotNull null
            ShortcutInfo.Builder(c, pkg).setShortLabel(name).setIcon(Icon.createWithBitmap(bmp)).setIntent(i).build()
        }
        runCatching { sm.dynamicShortcuts = list }
    }
}
