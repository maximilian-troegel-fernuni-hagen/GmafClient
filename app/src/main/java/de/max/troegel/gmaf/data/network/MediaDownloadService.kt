package de.max.troegel.gmaf.data.network

import android.graphics.Bitmap

interface MediaDownloadService {
    suspend fun loadBitmapFromUrl(url: String): Bitmap?
    suspend fun loadTextFromUrl(url: String): String?
}