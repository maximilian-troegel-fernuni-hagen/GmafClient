package de.max.troegel.gmaf.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.max.troegel.gmaf.app.CustomLogTree
import de.max.troegel.gmaf.app.MainActivity
import de.max.troegel.gmaf.data.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit


class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    fun onUrlChanged(url: String, context: Context) {
        settingsRepository.url = url
        Timber.log(Log.INFO, "Updated api url $url")
        viewModelScope.launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            restartApp(context)
        }
    }

    fun onQualityChanged(imageQuality: String) {
        settingsRepository.imageQuality = imageQuality
        Timber.log(Log.INFO, "Updated imageQuality $imageQuality")
    }

    fun onLoggingChanged(deactivateLogging: Boolean) {
        settingsRepository.deactivateLogging = deactivateLogging
        Timber.plant(CustomLogTree(deactivateLogging))
        Timber.log(Log.INFO, "Updated logging $deactivateLogging")
    }

    private fun restartApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
        Runtime.getRuntime().exit(0)
    }
}