package de.max.troegel.gmaf.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.QueryType
import de.max.troegel.gmaf.data.repository.QueryRepository
import de.max.troegel.gmaf.data.repository.SettingsRepository
import de.swa.gc.GraphCode
import timber.log.Timber
import java.util.*

class MainViewModel(
    private val queryRepository: QueryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    /**
     * Checks if the endpoint warning should be displayed
     */
    fun shouldDisplayEndpointWarning(): Boolean = !settingsRepository.wasEndpointSet()

    /**
     * Handles the action from the intent depending on the type
     */
    fun handleIntent(intent: Intent, context: Context) {
        when (intent.action) {
            Intent.ACTION_VIEW -> parseIntent(intent, context)
            Intent.ACTION_MAIN -> {
                // do nothing
            }
            else -> showErrorMessage(context)
        }
    }

    /**
     * Use extras provided by the intent to handle the different intents
     */
    private fun parseIntent(intent: Intent, context: Context) {
        val data = intent.data
        val type = intent.type
        val encodedPath = data?.encodedPath
        val searchDescription: String = data?.getQueryParameter("media_description") ?: ""
        Timber.log(Log.INFO, "Parse intent $data $type $encodedPath $searchDescription")
        if (encodedPath != null) {
            when (encodedPath) {
                "/search_similar_media/" -> {
                    setQuery(
                        GmafQuery(
                            Date().time,
                            QueryType.FIND_SIMILAR_MEDIA,
                            searchDescription,
                            generateGraphCodeFor(searchDescription)
                        )
                    )
                }
                "/search_recommended_media/" -> {
                    setQuery(
                        GmafQuery(
                            Date().time,
                            QueryType.FIND_RECOMMENDED_MEDIA,
                            searchDescription,
                            generateGraphCodeFor(searchDescription)
                        )
                    )
                }
                else -> {
                    showErrorMessage(context)
                }
            }
        } else {
            showErrorMessage(context)
        }
    }

    /**
     * Stores the new query to execute right now
     */
    private fun setQuery(query: GmafQuery?) {
        Timber.i("Set query $query")
        queryRepository.setQuery(query)
    }

    /**
     * Generates GraphCode for the given input text
     */
    private fun generateGraphCodeFor(inputText: String): GraphCode {

        val queryComponents: Array<String> = inputText.split(",", " ").toTypedArray()
        val dictionary = Vector<String>()
        for (queryComponent in queryComponents) {
            if (queryComponent.isNotEmpty()) {
                if (!dictionary.contains(queryComponent)) {
                    dictionary.add(queryComponent.trim { it <= ' ' })
                }
            }
        }
        val gcQuery = GraphCode()
        gcQuery.dictionary = dictionary
        return gcQuery
    }

    /**
     * Displays an error message
     */
    private fun showErrorMessage(context: Context) {
        val message = context.getString(R.string.query_creation_failed)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}