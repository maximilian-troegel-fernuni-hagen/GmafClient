package de.max.troegel.gmaf.app

import timber.log.Timber

class CustomLogTree(private val deactivateLogging: Boolean) : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (deactivateLogging) {
            return
        }
        super.log(priority, tag, message, t)
    }
}