package de.max.troegel.gmaf.contextprovider

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Collection of the available dispatch providers
 */
class DispatcherProviderImpl : DispatcherProvider {

    /**
     * The main DispatcherProvider that should be used for ui tasks
     */
    override val main: CoroutineDispatcher get() = Dispatchers.Main

    /**
     * The default DispatcherProvider that should be used for general tasks
     */
    override val default: CoroutineDispatcher get() = Dispatchers.Default

    /**
     * The io DispatcherProvider that should be used for filesystem tasks
     */
    override val io: CoroutineDispatcher get() = Dispatchers.IO

    /**
     * The unconfirmed DispatcherProvider that should be used for unconfirmed tasks
     */
    override val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
}
