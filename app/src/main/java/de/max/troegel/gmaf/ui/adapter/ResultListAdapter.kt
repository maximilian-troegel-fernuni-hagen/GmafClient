package de.max.troegel.gmaf.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.max.troegel.gmaf.app.loadWithThumbnail
import de.max.troegel.gmaf.data.model.GmafMedia
import de.max.troegel.gmaf.data.model.MediaType
import de.max.troegel.gmaf.databinding.ListItemPreviewBinding

/**
 * Adapter class [RecyclerView.Adapter] for [RecyclerView] which binds [GmafMedia] results.
 */
class ResultListAdapter(
    private val onItemClicked: (Int) -> Unit
) : ListAdapter<GmafMedia, RecyclerView.ViewHolder>(GmafImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemPreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GmafMediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaResult = getItem(position)
        (holder as GmafMediaViewHolder).bind(mediaResult, position, onItemClicked)
    }

    class GmafMediaViewHolder(private val binding: ListItemPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaResult: GmafMedia, position: Int, onItemClicked: (Int) -> Unit) {
            binding.root.setOnClickListener {
                onItemClicked(position)
            }
            binding.imageView.scaleType =
                if (mediaResult.mediaType == MediaType.IMAGE)
                    ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.CENTER_INSIDE
            binding.imageView.loadWithThumbnail(mediaResult.getPreview())
        }
    }
}

private class GmafImageDiffCallback : DiffUtil.ItemCallback<GmafMedia>() {

    override fun areItemsTheSame(oldItem: GmafMedia, newItem: GmafMedia): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GmafMedia, newItem: GmafMedia): Boolean {
        return oldItem == newItem
    }
}
