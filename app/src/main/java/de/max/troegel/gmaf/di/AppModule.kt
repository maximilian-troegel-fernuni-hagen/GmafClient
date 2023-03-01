package de.max.troegel.gmaf.di

import de.max.troegel.gmaf.contextprovider.DispatcherProviderImpl
import de.max.troegel.gmaf.contextprovider.DispatcherProvider
import org.koin.dsl.module

val appModule = module {

    /**
     * The DispatcherProvider instance
     */
    single<DispatcherProvider> { DispatcherProviderImpl() }
}