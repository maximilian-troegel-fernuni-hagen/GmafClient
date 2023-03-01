package de.max.troegel.gmaf.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.app.getNavigationParent
import de.max.troegel.gmaf.data.model.QueryType
import de.max.troegel.gmaf.databinding.FragmentSearchBinding
import de.max.troegel.gmaf.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.queryText.text = ""
        setupButtonObserver()
        setupQueryObserver()
        setupResultObserver()
        return binding.root
    }

    private fun setupButtonObserver() {
        binding.talkButton.setOnClickListener {
            if (context != null) {
                context?.let { context ->
                    if (viewModel.useApiStub() || true) {
                        // Execute an example intent against the api stub
                        viewModel.simulateIntent(context)
                    } else {
                        // Open the google assistant to execute a custom query
                        ContextCompat.startActivity(
                            context,
                            Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            null
                        )
                    }
                }
            } else {
                showErrorMessage()
            }
        }
    }

    private fun setupQueryObserver() {
        viewModel.query.observe(viewLifecycleOwner) { query ->
            if (query == null) {
                binding.queryText.text = ""
                binding.loadingProgress.visibility = GONE
            } else {
                val prefix = if (query.getQueryType() == QueryType.FIND_RECOMMENDED_MEDIA)
                    getString(R.string.search_recommended) else getString(R.string.search_similar_media)
                binding.queryText.text = prefix + " " + query.getQueryText() + " ..."
                viewModel.executeQuery(query)
                binding.loadingProgress.visibility = VISIBLE
                binding.loadingProgress.isIndeterminate = true
                binding.loadingProgress.progress = 50
            }
        }
    }

    private fun setupResultObserver() {
        viewModel.results.observe(viewLifecycleOwner) { results ->
            if (results != null) {
                binding.loadingProgress.visibility = GONE
                navigateToResultFragment()
            }
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(context, getString(R.string.query_creation_failed), Toast.LENGTH_SHORT).show()
    }

    private fun navigateToResultFragment() {
        getNavigationParent()?.switchToResultFragment()
    }
}
