package de.max.troegel.gmaf.data.model

import android.os.Parcelable
import de.swa.gc.GraphCode
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GmafImage(
    /**
     * The url of this result where the image data can be found
     */
    override val url: String,
    /**
     * The graph code of this result image
     */
    override val gc: GraphCode,
    /**
     * The image title - usually the original filename
     */
    val title: String,
    /**
     * The raw image data
     */
    val previewData: ByteArray
) : GmafMedia(MediaType.IMAGE, url, gc), Parcelable {

    override fun getPreview(): ByteArray {
        return previewData
    }

    override fun getData(): ByteArray {
        return previewData
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GmafImage

        if (title != other.title) return false
        if (!previewData.contentEquals(other.previewData)) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode() + title.hashCode()
    }
}
