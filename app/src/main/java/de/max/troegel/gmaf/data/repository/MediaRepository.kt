package de.max.troegel.gmaf.data.repository

import de.max.troegel.gmaf.data.io.MediaDownloadRemote
import de.max.troegel.gmaf.data.model.GmafMedia
import de.max.troegel.gmaf.data.model.GmafMediaList
import de.max.troegel.gmaf.data.model.Result
import de.swa.mmfg.MMFG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.*

/**
 * Repository module that provides access to the [GmafMedia] database
 */
class MediaRepository(
    private val remote: MediaDownloadRemote
) {
    val response: MutableStateFlow<Vector<MMFG?>?> = MutableStateFlow(null)

    /**
     * Sets the result MMFG list
     */
    fun setResponse(vector: Vector<MMFG?>?) {
        response.value = vector
    }

    /**
     * Downloads the result media data
     */
    fun loadResultMedia(results: Vector<MMFG?>?): Flow<Result<GmafMediaList>> = flow {
        emit(Result.loading())
        val resultMedia = remote.downloadMedia(results)
        emit(if (resultMedia != null) Result.success(resultMedia) else Result.empty())
    }.catch { e ->
        Timber.e(e)
        emit(Result.error(e))
    }
}
