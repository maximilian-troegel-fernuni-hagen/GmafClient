package de.max.troegel.gmaf.data.io

import android.graphics.Bitmap
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import de.max.troegel.gmaf.app.extractFileNameFromUrl
import de.max.troegel.gmaf.app.toData
import de.max.troegel.gmaf.contextprovider.DispatcherProvider
import de.max.troegel.gmaf.data.model.*
import de.max.troegel.gmaf.data.network.MediaDownloadService
import de.swa.gc.GraphCode
import de.swa.mmfg.MMFG
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*


/**
 * Default implementation of [MediaDownloadRemote].
 */
class MediaDownloadRemoteImpl(
    private val service: MediaDownloadService,
    private val dispatchers: DispatcherProvider
) : MediaDownloadRemote {

    class TestExclStrat : ExclusionStrategy {
        override fun shouldSkipClass(arg0: Class<*>?): Boolean {
            return false
        }

        override fun shouldSkipField(f: FieldAttributes): Boolean {
            return f.name == "bytes"
        }
    }

    var gson = GsonBuilder()
        .setExclusionStrategies(TestExclStrat())
        .serializeNulls()
        .create()

    /**
     * Downloads the media data from the given [results]
     *
     * @param results The list of downloaded media
     */
    override suspend fun downloadMedia(results: Vector<MMFG?>?): GmafMediaList = withContext(dispatchers.default) {
        val list = mutableListOf<GmafMedia>()
        if (results != null) {
            for (result in results) {
                if (result != null) {
                    try {
                        Timber.i("Processing result ${gson.toJson(result)}")
                        when (getMediaTypeOf(result.generalMetadata.fileName)) {
                            MediaType.IMAGE -> {
                                result.generalMetadata.bytes?.toImage(result.generalMetadata.fileName, result.generalMetadata.grapCode)?.let { image ->
                                    list.add(image)
                                }
                                /*
                            val response = service.loadBitmapFromUrl(url)
                            response?.toImage(url)?.let { image ->
                                list.add(image)
                            }*/
                            }
                            MediaType.AUDIO -> {
                                val url = result.locations?.first { it.location != null }?.location?.toString()
                                url?.toAudio(result.generalMetadata.grapCode)?.let { audio ->
                                    list.add(audio)
                                }
                            }
                            MediaType.DOCUMENT -> {
                                val url = result.locations?.first { it.location != null }?.location?.toString()
                                if (url != null) {
                                    val response = service.loadTextFromUrl(url)
                                    response?.toDocument(url, result.generalMetadata.grapCode)?.let { document ->
                                        list.add(document)
                                    }
                                }
                            }
                            else -> {
                                Timber.i("Could not process result ${result.generalMetadata.fileName}")
                            }
                        }
                    } catch (e: Exception) {
                        Timber.e(e)
                        Timber.i("Processing result ${result.generalMetadata.fileName} failed")
                    }
                }
            }
        }
        list
    }

    /**
     * Transform the bitmap into a GmafImage
     *
     * @param url The image url that is required to access the image file name
     */
    private fun Bitmap.toImage(url: String, gc: GraphCode = GraphCode()): GmafImage {
        return GmafImage(url, gc, url.extractFileNameFromUrl(), toData())
    }

    /**
     * Transform the bytearray into a GmafImage
     *
     * @param url The image url that is required to access the image file name
     */
    private fun ByteArray.toImage(url: String, gc: GraphCode = GraphCode()): GmafImage {
        return GmafImage(url, gc, url.extractFileNameFromUrl(), this)
    }

    /**
     * Transform the string into a GmafDocument
     *
     * @param url The document url that is required to access the document file name
     */
    private fun String.toDocument(url: String, gc: GraphCode = GraphCode()): GmafDocument {
        return GmafDocument(url, gc, url.extractFileNameFromUrl(), this.toByteArray(Charsets.UTF_8))
    }

    /**
     * Transform the url string into a GmafAudio
     */
    private fun String.toAudio(gc: GraphCode = GraphCode()): GmafAudio {
        return GmafAudio(this, gc, this.extractFileNameFromUrl(), this.toByteArray(Charsets.UTF_8))
    }

    private fun getMediaTypeOf(fileName: String): MediaType? {
        return when (fileName.substringAfterLast(".").lowercase()) {
            "jpg" -> MediaType.IMAGE
            "png" -> MediaType.IMAGE
            "mp3" -> MediaType.AUDIO
            "json" -> MediaType.DOCUMENT
            "txt" -> MediaType.DOCUMENT
            else -> null
        }
    }
}
