package de.max.troegel.gmaf.data.model

import android.os.Parcelable
import de.max.troegel.gmaf.util.documentPreviewIcon
import de.swa.gc.GraphCode
import kotlinx.android.parcel.Parcelize

@Parcelize
class GmafDocument(
    /**
     * The url of this result where the document data can be found
     */
    override val url: String,
    /**
     * The graph code of this result document
     */
    override val gc: GraphCode,
    /**
     * The document title - usually the original filename
     */
    val title: String,
    /**
     * The raw document data as utf 8 encoded byte array
     */
    private val documentData: ByteArray
) : GmafMedia(MediaType.DOCUMENT, url, gc), Parcelable {

    override fun getPreview(): ByteArray = documentPreviewIcon

    override fun getData(): ByteArray {
        return documentData
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GmafDocument

        if (title != other.title) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode() + title.hashCode()
    }
}