package de.max.troegel.gmaf.data.io

import de.max.troegel.gmaf.contextprovider.DispatcherProvider
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.QueryType
import de.max.troegel.gmaf.data.network.GmafRestService
import de.swa.mmfg.MMFG
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class GmafRestRemoteImpl(
    private val service: GmafRestService,
    private val dispatchers: DispatcherProvider
) : GmafRestRemote {

    private var authToken = ""

    private suspend fun getAuthToken(): String = withContext(dispatchers.default) {
        if (authToken.isEmpty()) {
            updateAuthToken()
        }
        authToken
    }

    override suspend fun updateAuthToken() = withContext(dispatchers.default) {
        try {
            authToken = service.getAuthToken("fp2223")
        } catch (e: java.lang.Exception) {
            Timber.e(e)
        }
    }

    override suspend fun executeQuery(query: GmafQuery): Vector<MMFG?>? = withContext(dispatchers.default) {
        try {
            Timber.i("Execute query $query")
            when (query.getQueryType()) {
                QueryType.FIND_RECOMMENDED_MEDIA ->
                    service.getRecommendedAssets(getAuthToken(), query.getGraphCode())
                QueryType.FIND_SIMILAR_MEDIA ->
                    service.getSimilarAssets(getAuthToken(), query.getGraphCode())
            }
        } catch (e: java.lang.Exception) {
            Timber.e(e)
            null
        }
    }
}