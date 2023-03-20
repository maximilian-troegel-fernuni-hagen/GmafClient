package de.max.troegel.gmaf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import de.max.troegel.gmaf.data.model.GmafMediaList
import de.max.troegel.gmaf.data.model.Result
import de.max.troegel.gmaf.data.repository.MediaRepository
import de.swa.mmfg.MMFG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import java.util.*

class GalleryViewModel(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val response: MutableStateFlow<Vector<MMFG?>?> = mediaRepository.response

    val results: LiveData<Result<GmafMediaList>> = response.debounce(DEBOUNCE_MILLIS).flatMapLatest { response ->
        mediaRepository.loadResultMedia(response)
    }.asLiveData()
}

private const val DEBOUNCE_MILLIS = 700L