package de.max.troegel.gmaf.data.io

import de.max.troegel.gmaf.data.model.GmafMediaList
import de.max.troegel.gmaf.data.model.GmafQuery
import de.swa.mmfg.MMFG
import java.util.*

/**
 * Remote entry point to fetch images from the web
 */
interface MediaDownloadRemote {

    /**
     * Download the image data from the given [urls]
     *
     * @param urls The list of image urls (should point to a png or jpg)
     */
    suspend fun downloadMedia(results: Vector<MMFG?>?): GmafMediaList?
}
