package de.max.troegel.gmaf.data.model

import java.io.IOException

/**
 * Result with builtin management for async loading of UI and data
 */
sealed class Result<out T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error(val exception: Throwable) : Result<Nothing>() {

        val isNetworkError: Boolean get() = exception is IOException
    }

    object Empty : Result<Nothing>()

    object Loading : Result<Nothing>()

    companion object {

        fun <T> success(data: T) = Success(data)

        fun error(exception: Throwable) = Error(exception)

        fun empty() = Empty

        fun loading() = Loading

        /**
         * Returns [Empty] if [list] is empty, otherwise [Success]
         */
        fun <T> successOrEmpty(list: List<T>): Result<List<T>> {
            return if (list.isEmpty()) Empty else Success(list)
        }
    }
}
