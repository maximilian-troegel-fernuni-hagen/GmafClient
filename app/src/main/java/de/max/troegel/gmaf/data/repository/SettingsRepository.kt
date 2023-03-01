package de.max.troegel.gmaf.data.repository

import android.content.Context
import androidx.preference.PreferenceManager
import de.max.troegel.gmaf.R

class SettingsRepository(context: Context) {

    var url = ""
    var imageQuality = ""
    var deactivateLogging = false

    fun wasEndpointSet(): Boolean = url.trim().isNotEmpty()

    init {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        url = preferences.getString("pref_api_url", "") ?: ""
        imageQuality = preferences.getString("pref_image_quality", context.getString(R.string.pref_image_default)) ?: context.getString(R.string.pref_image_default)
        deactivateLogging = preferences.getBoolean("pref_deactivate_logging", false)
    }
}