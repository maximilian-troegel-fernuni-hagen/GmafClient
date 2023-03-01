package de.max.troegel.gmaf.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import de.max.troegel.gmaf.app.load
import de.max.troegel.gmaf.data.model.GmafAudio
import de.max.troegel.gmaf.data.model.GmafDocument
import de.max.troegel.gmaf.data.model.GmafImage
import de.max.troegel.gmaf.data.model.GmafMedia
import de.max.troegel.gmaf.databinding.ListItemGalleryBinding


/**
 * Adapter class [RecyclerView.Adapter] for [ViewPager2] which binds [GmafMedia]
 */
class ExpandedResultAdapter(
    private val onItemClicked: () -> Unit,
    private val onInfoButtonClicked: (GmafMedia) -> Unit,
    private val onBackButtonClicked: () -> Unit,
    private val results: MutableList<GmafMedia> = mutableListOf(),
    private var currentPosition: Int = 0
) : RecyclerView.Adapter<ExpandedResultAdapter.ViewHolder>() {

    private var player: ExoPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, parent.context, ::getPlayer)
    }

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position], onItemClicked, onInfoButtonClicked, onBackButtonClicked)
    }

    fun submitList(list: List<GmafMedia>) {
        this.results.clear()
        this.results.addAll(list)
        notifyDataSetChanged()
    }

    fun onPageChanged(position: Int) {
        if (position != currentPosition) {
            currentPosition = position
            when (val media = results[position]) {
                is GmafImage -> {
                    resetPlayer()
                }
                is GmafAudio -> {
                    player?.let {
                        setupPlayer(it, media)
                    }
                }
                is GmafDocument -> {
                    resetPlayer()
                }
            }
        }
    }

    class ViewHolder(
        private val binding: ListItemGalleryBinding,
        private val context: Context,
        private val getPlayer: (context: Context, media: GmafMedia) -> ExoPlayer
    ):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            media: GmafMedia,
            onItemClicked: () -> Unit,
            onInfoButtonClicked: (GmafMedia) -> Unit,
            onBackButtonClicked: () -> Unit) {
            binding.imageView.setOnClickListener {
                onItemClicked()
            }
            binding.backButton.setOnClickListener {
                onBackButtonClicked()
            }
            binding.infoButton.setOnClickListener {
                onInfoButtonClicked(media)
            }
            when (media) {
                is GmafImage -> {
                    binding.playerView.visibility = GONE
                    binding.textView.visibility = GONE
                    binding.imageView.visibility = VISIBLE
                    binding.imageView.load(media.getData())
                }
                is GmafAudio -> {
                    binding.imageView.visibility = GONE
                    binding.textView.visibility = GONE
                    binding.playerView.visibility = VISIBLE
                    binding.playerView.player = getPlayer(context, media)
                    binding.playerView.controllerHideOnTouch = false
                    binding.playerView.controllerAutoShow = true
                    binding.playerView.useController = true
                    binding.playerView.controllerShowTimeoutMs = -1
                    binding.playerView.showController()
                }
                is GmafDocument -> {
                    binding.playerView.visibility = GONE
                    binding.imageView.visibility = GONE
                    binding.textView.visibility = VISIBLE
                    binding.textView.text = media.getData().toString(Charsets.UTF_8)
                }
            }
        }
    }

    private fun getPlayer(context: Context, media: GmafMedia): ExoPlayer {
        player?.clearMediaItems() ?: run {
            player = ExoPlayer.Builder(context).build()
        }
        setupPlayer(player!!, media)
        return player!!
    }

    fun resetPlayer() {
        player?.clearMediaItems()
    }

    private fun setupPlayer(player: ExoPlayer, media: GmafMedia) {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
        val audioSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(media.getData().toString(Charsets.UTF_8))))
        player.addMediaSource(audioSource)
        player.playWhenReady = true
        player.prepare()
        player.play()
    }
}
