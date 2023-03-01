package de.max.troegel.gmaf.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.max.troegel.gmaf.data.io.GmafRestRemote
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.repository.MediaRepository
import de.max.troegel.gmaf.data.repository.QueryRepository
import de.max.troegel.gmaf.data.repository.SettingsRepository
import de.swa.mmfg.MMFG
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class SearchViewModel(
    queryRepository: QueryRepository,
    private val mediaRepository: MediaRepository,
    private val settingsRepository: SettingsRepository,
    private val gmafRestRemote: GmafRestRemote
) : ViewModel() {

    val query: LiveData<GmafQuery?> = queryRepository.activeQuery

    val results: LiveData<Vector<MMFG?>?> = mediaRepository.response.asLiveData()

    fun useApiStub(): Boolean = !settingsRepository.wasEndpointSet()

    fun simulateIntent(context: Context) {
        Timber.log(Log.INFO, "Simulate intent}")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.`package` = "de.max.troegel.gmaf"
        intent.data = Uri.parse("http://www.gmaf.de/search_recommended_media/?media_description=swimming+animals")
        context.startActivity(intent)
    }

    fun executeQuery(query: GmafQuery) {
        Timber.log(Log.INFO, "Execute query ${query.getQueryType()} ${query.getQueryText()}")
        viewModelScope.launch {
            val result = gmafRestRemote.executeQuery(query)
            mediaRepository.setResponse(result)
        }
    }
}