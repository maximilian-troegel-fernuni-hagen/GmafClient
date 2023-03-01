package de.max.troegel.gmaf.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences, key: String) {
        when (key) {
            "pref_api_url" -> {
                preferences.getString(key, "")?.let { url ->
                    context?.let { context -> viewModel.onUrlChanged(url, context) }
                }
            }
            "pref_image_quality" -> {
                preferences.getString(key, "")?.let { quality ->
                    viewModel.onQualityChanged(quality)
                }
            }
            "pref_deactivate_logging" -> {
                viewModel.onLoggingChanged(preferences.getBoolean(key, false))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}
