package de.max.troegel.gmaf.app

import android.app.Application
import androidx.preference.PreferenceManager
import de.max.troegel.gmaf.di.appModule
import de.max.troegel.gmaf.di.networkModule
import de.max.troegel.gmaf.di.repositoryModule
import de.max.troegel.gmaf.di.viewModelModule
import gurtek.com.offlinedictionary.Dictionary
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * The main application that initializes all application components
 */
class GmafApplication : Application() {

    /**
     * Configure the application setup
     */
    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupTimber()
        setupDictionary()
    }

    /**
     * Configures dependency injection
     */
    private fun setupKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GmafApplication)
            modules(
                appModule,
                viewModelModule,
                repositoryModule,
                networkModule
            )
        }
    }

    /**
     * Configures logging and failure handling
     */
    private fun setupTimber() {
        Timber.plant(
            CustomLogTree(
                PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                    "pref_deactivate_logging", false
                )
            )
        )
    }

    /**
     * Loads the local dictionary
     */
    private fun setupDictionary() {
        Dictionary.init(this)
        val progress = PublishSubject.create<Int>()
        Dictionary.getEnglishDictionary().importDbFileFromAssets(progress).subscribe()
        progress.subscribe { percent ->
            Timber.i("Loaded dictionary db: $percent%")
        }
    }
}
