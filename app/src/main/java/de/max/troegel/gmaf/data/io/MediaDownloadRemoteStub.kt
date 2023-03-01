package de.max.troegel.gmaf.data.io

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.app.*
import de.max.troegel.gmaf.contextprovider.DispatcherProvider
import de.max.troegel.gmaf.data.model.*
import de.swa.gc.GraphCode
import de.swa.mmfg.MMFG
import kotlinx.coroutines.withContext
import okhttp3.internal.immutableListOf
import timber.log.Timber
import java.util.*


class MediaDownloadRemoteStub(
    private val dispatchers: DispatcherProvider,
    private val context: Context
) : MediaDownloadRemote {

    private val random = Random()

    /**
     * Download the image data from the given [urls]
     *
     * @param urls The list of image urls (should point to a png or jpg)
     */
    override suspend fun downloadMedia(results: Vector<MMFG?>?): GmafMediaList {
        return withContext(dispatchers.io) {
            mutableListOf(
                createGmafImageStub("0", R.drawable.test_image_0423, immutableListOf("turtle", "animal", "grass", "brown")),
                createGmafImageStub("1", R.drawable.test_image_0472, immutableListOf("fish", "water", "swimming")),
                createGmafAudioStub("2", "http://www.openmusicarchive.org/audio/April_Kisses.mp3"),
                createGmafDocumentStub("3", "https://github.com/ljharb/json-file-plus/blob/main/package.json", "This is a nice document.\nThe text is very short."),
                createGmafImageStub("4", R.drawable.test_image_0003, immutableListOf("palms", "pool", "holiday", "water")),
                createGmafImageStub("5", R.drawable.test_image_0406, immutableListOf("child", "paper", "boxes", "crayons")),
                createGmafImageStub("6", R.drawable.test_image_0417, immutableListOf("art", "people", "street")),
                createGmafImageStub("7", R.drawable.test_image_0453, immutableListOf("window", "green")),
                createGmafImageStub("8", R.drawable.test_image_0660, immutableListOf("sculpture")),
                createGmafImageStub("9", R.drawable.test_image_0668, immutableListOf("backpack", "mountain", "hiking", "clouds"))
            )
        }
    }

    private fun createGmafImageStub(title: String, resourceId: Int, keyWords: List<String>): GmafImage {
        val drawable = ContextCompat.getDrawable(context, resourceId)
        val previewData = drawable?.toBitmap(
            drawable.getPreviewWidth(),
            drawable.getPreviewHeight()
        )?.toData()
        val graphCode = GraphCode()
        try {
            val dictionary = Vector<String>()
            keyWords.forEach { element ->
                dictionary.add(element)
            }
            graphCode.dictionary = dictionary
            for (i in keyWords.indices) {
                for (a in keyWords.indices) {
                    graphCode.setValue(i, a, random.nextInt(keyWords.size))
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return GmafImage(title, graphCode, title, previewData!!)
    }

    private fun createGmafAudioStub(title: String, url: String): GmafAudio {
        return GmafAudio(title, GraphCode(), title, url.toByteArray(Charsets.UTF_8))
    }

    private fun createGmafDocumentStub(title: String, url: String, text: String): GmafDocument {
        return GmafDocument(url, GraphCode(), title, text.toByteArray(Charsets.UTF_8))
    }
}
