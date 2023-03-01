package de.max.troegel.gmaf.data.network

import de.swa.gc.GraphCode
import de.swa.mmfg.MMFG
import retrofit2.http.*
import java.util.*

interface GmafRestService {

    @GET("getAuthToken/{apiKey}")
    suspend fun getAuthToken(
        @Path("apiKey") apiKey: String
    ): String

    @POST("getSimilarAssets/{authToken}/{gc}")
    suspend fun getSimilarAssets(
        @Path("authToken") authToken: String,
        @Path("gc") gc: GraphCode
    ): Vector<MMFG?>?

    @POST("getRecommendedAssets/{authToken}/{gc}")
    suspend fun getRecommendedAssets(
        @Path("authToken") authToken: String,
        @Path("gc") gc: GraphCode
    ): Vector<MMFG?>?
}