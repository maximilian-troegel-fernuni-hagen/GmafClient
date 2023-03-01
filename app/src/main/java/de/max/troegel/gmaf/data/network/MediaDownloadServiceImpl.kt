package de.max.troegel.gmaf.data.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.ConnectException
import java.net.URL

class MediaDownloadServiceImpl : MediaDownloadService {

    override suspend fun loadBitmapFromUrl(url: String): Bitmap? {
        return try {
            BitmapFactory.decodeStream(withContext(Dispatchers.IO) {
                URL(url).openConnection().getInputStream()
            })
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    override suspend fun loadTextFromUrl(url: String): String? {
        return try {
            URL(url).readText()
        } catch (e: ConnectException) {
            Timber.e(e)
            null
        }
    }
}