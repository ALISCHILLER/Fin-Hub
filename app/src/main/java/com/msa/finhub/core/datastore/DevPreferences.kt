package com.msa.finhub.core.datastore



import android.content.Context

/**
 * Developer-only preferences for runtime configuration tweaks.
 */
object DevPreferences {
    private const val PREFS_NAME = "dev_prefs"
    private const val KEY_BASE_URL = "base_url"
    const val DEFAULT_BASE_URL = "http://10.252.112.93:8282/"

    fun getBaseUrl(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_BASE_URL, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
    }

    fun setBaseUrl(context: Context, url: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_BASE_URL, url).apply()
    }
}