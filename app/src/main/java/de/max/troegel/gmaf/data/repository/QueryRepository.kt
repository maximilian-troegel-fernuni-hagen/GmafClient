package de.max.troegel.gmaf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import de.max.troegel.gmaf.data.model.GmafQuery
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

/**
 * Repository module that provides access to the [GmafQuery] database
 */
@OptIn(FlowPreview::class)
class QueryRepository {
    private val activeQueryFlow = MutableStateFlow<GmafQuery?>(null)

    val activeQuery: LiveData<GmafQuery?> = activeQueryFlow.debounce(DEBOUNCE_MILLIS).asLiveData()

    /**
     * Sets the new [query] to execute
     */
    fun setQuery(query: GmafQuery?) {
        activeQueryFlow.value = query
    }
}
private const val DEBOUNCE_MILLIS = 700L