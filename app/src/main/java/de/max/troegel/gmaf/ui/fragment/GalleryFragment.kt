package de.max.troegel.gmaf.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import de.max.troegel.gmaf.data.model.GmafMedia
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.Result
import de.max.troegel.gmaf.databinding.FragmentGalleryBinding
import de.max.troegel.gmaf.ui.adapter.ExpandedResultAdapter
import de.max.troegel.gmaf.viewmodel.GalleryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: GalleryViewModel by viewModel()
    private lateinit var query: GmafQuery
    private val args: GalleryFragmentArgs by navArgs()

    private val expandedResultAdapter = ExpandedResultAdapter(
        this::onItemClicked,
        this::onInfoButtonClicked,
        this::onBackButtonClicked
    )

    private var onPageChangeCallback: PageChangeCallback? = null

    class PageChangeCallback(val onPageChanged: (Int) -> Unit) : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            onPageChanged(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        setupViewPager()
        setupFullscreen()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        expandedResultAdapter.resetPlayer()
        exitFullscreen()
        onPageChangeCallback?.let { binding.viewPager.unregisterOnPageChangeCallback(it) }
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = expandedResultAdapter
        viewModel.results.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    expandedResultAdapter.submitList(result.data)
                    val position = args.position
                    binding.viewPager.setCurrentItem(position, false)
                    onPageChangeCallback = PageChangeCallback(this::onPageChanged)
                    binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback!!)
                    query = args.query
                }
                else -> {}
            }
        }
    }

    private fun setupFullscreen() {
        activity?.window?.addFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun exitFullscreen() {
        activity?.window?.clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun onItemClicked() {
        findNavController().navigateUp()
    }

    private fun onInfoButtonClicked(media: GmafMedia) {
        val direction =
            GalleryFragmentDirections.galleryFragmentToDetailsFragment(media, query)
        findNavController().navigate(direction)
    }

    private fun onBackButtonClicked() {
        findNavController().navigateUp()
    }

    private fun onPageChanged(position: Int) {
        expandedResultAdapter.onPageChanged(position)
    }
}
