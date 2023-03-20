package de.max.troegel.gmaf.di

import de.max.troegel.gmaf.data.io.GmafRestRemote
import de.max.troegel.gmaf.data.repository.MediaRepository
import de.max.troegel.gmaf.data.repository.QueryRepository
import de.max.troegel.gmaf.data.repository.SettingsRepository
import de.max.troegel.gmaf.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    /**
     * MainViewModel instance
     */
    viewModel { provideMainViewModel(get(), get(), get()) }
    /**
     * SearchViewModel instance
     */
    viewModel { provideSearchViewModel(get(), get(), get(), get()) }
    /**
     * ResultViewModel instance
     */
    viewModel { provideResultViewModel(get(), get()) }
    /**
     * GalleryViewModel instance
     */
    viewModel { provideGalleryViewModel(get()) }
    /**
     * DetailsViewModel instance
     */
    viewModel { provideDetailsViewModel() }
    /**
     * SettingsViewModel instance
     */
    viewModel { provideSettingsViewModel(get()) }
}

private fun provideMainViewModel(
    queryRepository: QueryRepository,
    mediaRepository: MediaRepository,
    settingsRepository: SettingsRepository
): MainViewModel {
    return MainViewModel(queryRepository, mediaRepository, settingsRepository)
}

private fun provideSearchViewModel(
    queryRepository: QueryRepository,
    mediaRepository: MediaRepository,
    settingsRepository: SettingsRepository,
    remote: GmafRestRemote
): SearchViewModel {
    return SearchViewModel(queryRepository, mediaRepository, settingsRepository, remote)
}

private fun provideResultViewModel(
    queryRepository: QueryRepository,
    mediaRepository: MediaRepository
): ResultViewModel {
    return ResultViewModel(queryRepository, mediaRepository)
}

private fun provideGalleryViewModel(
    mediaRepository: MediaRepository
): GalleryViewModel {
    return GalleryViewModel(mediaRepository)
}

private fun provideDetailsViewModel(): DetailsViewModel {
    return DetailsViewModel()
}

private fun provideSettingsViewModel(settingsRepository: SettingsRepository): SettingsViewModel {
    return SettingsViewModel(settingsRepository)
}