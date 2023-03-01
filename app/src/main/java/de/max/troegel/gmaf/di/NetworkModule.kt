package de.max.troegel.gmaf.di

import android.content.Context
import com.google.gson.GsonBuilder
import de.max.troegel.gmaf.BuildConfig
import de.max.troegel.gmaf.contextprovider.DispatcherProvider
import de.max.troegel.gmaf.data.io.*
import de.max.troegel.gmaf.data.network.GmafRestService
import de.max.troegel.gmaf.data.network.MediaDownloadService
import de.max.troegel.gmaf.data.network.MediaDownloadServiceImpl
import de.max.troegel.gmaf.data.repository.SettingsRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

// This is the default url for android emulators
// It points to the default spark rest api of the gmaf
// More information about spark: https://github.com/perwendel/spark
private const val FALLBACK_URL = "http://10.0.2.2:8481/gmaf/gmafApi/"

val networkModule = module {

    /**
     * The HttpLoggingInterceptor instance that can be used to analyse network requests and responses
     */
    single { provideLoggingInterceptor() }
    /**
     * The OkHttpClient instance that is used to build a retrofit client
     */
    single { provideOkHttpClient(get()) }
    /**
     * The Retrofit instance that is used to perform network operations like rest requests
     */
    single { provideRetrofit(get(), get()) }
    /**
     * The GmafRestService instance that is used to manage the gmaf rest remote access
     */
    single { provideGmafRestService(get()) }
    /**
     * The DownloadService instance that is used to manage the media download remote access
     */
    single { provideMediaDownloadService() }
    /**
     * MediaDownloadRemote instance that is used to download media files to the local storage
     */
    single { provideMediaDownloadRemote(get(), get(), get(), get()) }
    /**
     * The GmafRestInstance that is used to execute queries against the hosted gmaf
     */
    single { provideGmafRestRemote(get(), get(), get()) }
}

private fun provideMediaDownloadService(): MediaDownloadService {
    return MediaDownloadServiceImpl()
}

private fun provideLoggingInterceptor(): Interceptor {
    return HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}

private fun provideOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, settingsRepository: SettingsRepository): Retrofit {
    val gson = GsonBuilder()
        .setLenient()
        .create()
    return try {
        Retrofit.Builder()
            .baseUrl(settingsRepository.url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    } catch (e: IllegalArgumentException) {
        Timber.e(e)
        Retrofit.Builder()
            .baseUrl(FALLBACK_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}

private fun provideGmafRestService(retrofit: Retrofit): GmafRestService {
    return retrofit.create(GmafRestService::class.java)
}

private fun provideMediaDownloadRemote(
    settingsRepository: SettingsRepository,
    service: MediaDownloadService,
    dispatchers: DispatcherProvider,
    context: Context
): MediaDownloadRemote {
    return if (settingsRepository.wasEndpointSet()) provideMediaDownloadRemoteImpl(service, dispatchers) else provideMediaDownloadRemoteStub(dispatchers, context)
}


private fun provideMediaDownloadRemoteImpl(
    service: MediaDownloadService,
    dispatchers: DispatcherProvider
): MediaDownloadRemote {
    return MediaDownloadRemoteImpl(service, dispatchers)
}

private fun provideGmafRestRemote(
    settingsRepository: SettingsRepository,
    service: GmafRestService,
    dispatchers: DispatcherProvider
): GmafRestRemote {
    return if (settingsRepository.wasEndpointSet()) provideGmafRestRemoteImpl(service, dispatchers) else provideGmafRestRemoteStub()
}

private fun provideGmafRestRemoteImpl(
    service: GmafRestService,
    dispatchers: DispatcherProvider
): GmafRestRemote {
    return GmafRestRemoteImpl(service, dispatchers)
}

private fun provideGmafRestRemoteStub(): GmafRestRemote {
    return GmafRestRemoteStub()
}

private fun provideMediaDownloadRemoteStub(
    dispatchers: DispatcherProvider,
    context: Context
): MediaDownloadRemoteStub {
    return MediaDownloadRemoteStub(dispatchers, context)
}