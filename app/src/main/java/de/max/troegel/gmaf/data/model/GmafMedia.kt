package de.max.troegel.gmaf.data.model

import android.os.Parcelable
import de.swa.gc.GraphCode

abstract class GmafMedia(open var mediaType: MediaType, open val url: String, open val gc: GraphCode) : Parcelable {

    abstract fun getPreview(): ByteArray

    abstract fun getData(): ByteArray

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GmafMedia

        return mediaType == other.mediaType && gc == other.gc
    }

    override fun hashCode(): Int {
        return mediaType.hashCode() + gc.hashCode() + getData().hashCode()
    }
}

typealias GmafMediaList = List<GmafMedia>