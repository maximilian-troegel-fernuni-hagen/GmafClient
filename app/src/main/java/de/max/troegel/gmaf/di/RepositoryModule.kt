package de.max.troegel.gmaf.di

import android.content.Context
import de.max.troegel.gmaf.data.io.MediaDownloadRemote
import de.max.troegel.gmaf.data.repository.MediaRepository
import de.max.troegel.gmaf.data.repository.QueryRepository
import de.max.troegel.gmaf.data.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    /**
     * The query repository instance
     */
    single { provideQueryRepository() }

    /**
     * The media repository instance
     */
    single { provideMediaRepository(get()) }

    /**
     * The settings repository instance
     */
    single { provideSettingsRepository(androidContext()) }
}

private fun provideQueryRepository(): QueryRepository {
    return QueryRepository()
}

private fun provideMediaRepository(
    remote: MediaDownloadRemote
): MediaRepository {
    return MediaRepository(remote)
}

private fun provideSettingsRepository(context: Context): SettingsRepository {
    return SettingsRepository(context)
}
