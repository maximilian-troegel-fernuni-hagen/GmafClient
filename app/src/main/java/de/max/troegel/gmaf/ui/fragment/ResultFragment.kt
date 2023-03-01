package de.max.troegel.gmaf.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.app.fromDict
import de.max.troegel.gmaf.data.model.GmafMediaList
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.QueryType
import de.max.troegel.gmaf.data.model.Result
import de.max.troegel.gmaf.databinding.FragmentResultBinding
import de.max.troegel.gmaf.ui.adapter.ResultListAdapter
import de.max.troegel.gmaf.viewmodel.ResultViewModel
import de.swa.gc.GraphCode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding

    private val viewModel: ResultViewModel by viewModel()
    private val resultAdapter = ResultListAdapter(this::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        showLoadingView()
        setupImagesRecyclerView()
        setupSwipeRefreshLayout()
        setupResultObserver()
        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Shows the loading view
     */
    private fun showLoadingView() {
        with(binding) {
            val message = getString(R.string.loading_results_to_show)
            swipeRefreshLayout.isRefreshing = true
            imagesRecyclerView.visibility = View.INVISIBLE
            noDataGroup.visibility = View.VISIBLE
            noDataText.text = message
        }
        resultAdapter.submitList(emptyList())
    }

    /**
     * Shows the empty view
     */
    private fun showEmptyView(message: String) {
        with(binding) {
            swipeRefreshLayout.isRefreshing = false
            imagesRecyclerView.visibility = View.INVISIBLE
            noDataGroup.visibility = View.VISIBLE
            noDataText.text = message
        }
        resultAdapter.submitList(emptyList())
    }

    /**
     * Shows the recycler view and updates its list
     */
    private fun showResultList(media: GmafMediaList) {
        with(binding) {
            swipeRefreshLayout.isRefreshing = false
            imagesRecyclerView.visibility = View.VISIBLE
            noDataGroup.visibility = View.GONE
        }
        resultAdapter.submitList(media)
    }

    private fun setupImagesRecyclerView() {
        binding.imagesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = resultAdapter
        }
    }

    private fun onItemClicked(position: Int) {
        val images = resultAdapter.currentList.toTypedArray()
        val direction =
            HomeViewPagerFragmentDirections.homeViewPagerFragmentToGalleryFragment(
                images, position, viewModel.query.value ?: GmafQuery(Date().time, QueryType.FIND_RECOMMENDED_MEDIA, "water animals", GraphCode().fromDict(mutableListOf("fish", "water", "swimming")))
            )
        findNavController().navigate(direction)
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.isEnabled = false
    }

    private fun setupResultObserver() {
        viewModel.results.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoadingView()
                }
                is Result.Success -> {
                    showResultList(result.data)
                }
                is Result.Empty -> {
                    val message = getString(R.string.no_results_to_show)
                    showEmptyView(message)
                }
                is Result.Error -> {
                    val message = if (result.isNetworkError) {
                        getString(R.string.no_internet)
                    } else {
                        getString(R.string.no_results_to_show)
                    }
                    showEmptyView(message)
                }
            }

        }
    }
}
