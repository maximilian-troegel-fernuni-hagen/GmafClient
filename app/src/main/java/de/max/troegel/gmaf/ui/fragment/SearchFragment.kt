package de.max.troegel.gmaf.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
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
        binding.simulateExampleQuery.visibility = if (viewModel.useApiStub()) View.VISIBLE else View.GONE
        setupButtonObserver()
        setupQueryObserver()
        setupResultObserver()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_help, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.action_help -> showExampleIntentDialog()
            else -> false
        }
    }

    private fun showExampleIntentDialog(): Boolean {
        context?.let { context ->
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Example intents")
            val message = TextView(context)
            message.setPadding(10)
            val string = SpannableString(
                "http://www.gmaf.de/search_similar_media/?media_description=swimming+animals"
                        + "\n\n" + "http://www.gmaf.de/search_similar_media/?media_description=a+vacation+with+palm+trees"
                        + "\n\n" + "http://www.gmaf.de/search_similar_media/?media_description=picture"
                        + "\n\n" + "http://www.gmaf.de/search_similar_media/?media_description=a+fish"
            )
            Linkify.addLinks(string, Linkify.WEB_URLS)
            message.text = string
            message.movementMethod = LinkMovementMethod.getInstance()
            val container = FrameLayout(context)
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.leftMargin = 45
            params.rightMargin = 45
            message.layoutParams = params
            container.addView(message)
            builder.setView(container)
            builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
        return true
    }

    private fun setupButtonObserver() {
        binding.simulateExampleQuery.setOnClickListener {
            if (context != null) {
                context?.let { context ->
                    if (viewModel.useApiStub()) {
                        // Execute an example intent against the api stub
                        viewModel.simulateIntent(context)
                    }
                }
            }
        }
        binding.talkButton.setOnClickListener {
            if (context != null) {
                context?.let { context ->
                    // Open the google assistant to execute a custom query
                    ContextCompat.startActivity(
                        context,
                        Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        null
                    )
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
