package com.nexus.gamehub.feature.favorites
import android.content.Context
object FavoriteStore {
    fun get(c: Context): Set<String> = HashSet(c.getSharedPreferences("fav", 0).getStringSet("f", emptySet()) ?: emptySet())
    fun toggle(c: Context, pkg: String): Set<String> { val s = HashSet(get(c)); if (!s.add(pkg)) s.remove(pkg); c.getSharedPreferences("fav", 0).edit().putStringSet("f", s).apply(); return s }
}
