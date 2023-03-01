package de.max.troegel.gmaf.contextprovider

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Collection of the available dispatch providers
 */
interface DispatcherProvider {

    /**
     * The main DispatcherProvider that should be used for ui tasks
     */
    val main: CoroutineDispatcher

    /**
     * The default DispatcherProvider that should be used for general tasks
     */
    val default: CoroutineDispatcher

    /**
     * The io DispatcherProvider that should be used for filesystem tasks
     */
    val io: CoroutineDispatcher

    /**
     * The unconfirmed DispatcherProvider that should be used for unconfirmed tasks
     */
    val unconfined: CoroutineDispatcher
}
