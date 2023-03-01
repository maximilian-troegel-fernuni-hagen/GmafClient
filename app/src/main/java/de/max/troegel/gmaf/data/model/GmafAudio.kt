package de.max.troegel.gmaf.data.model

import android.os.Parcelable
import de.max.troegel.gmaf.util.audioPreviewIcon
import de.swa.gc.GraphCode
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GmafAudio(
    /**
     * The url of this result where the audio data can be found
     */
    override val url: String,
    /**
     * The graph code of this result audio
     */
    override val gc: GraphCode,
    /**
     * The audio title - usually the original filename
     */
    val title: String,
    /**
     * The raw audio data as encoded url
     */
    val audioData: ByteArray
) : GmafMedia(MediaType.AUDIO, url, gc), Parcelable {

    override fun getPreview() = audioPreviewIcon

    override fun getData(): ByteArray {
        return audioData
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GmafAudio

        if (title != other.title) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode() + title.hashCode()
    }
}