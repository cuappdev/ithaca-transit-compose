package com.cornellappdev.transit.networking

/**
 * Wrapper for data returned from an API
 */
sealed class ApiResponse<out T : Any> {
    object Pending : ApiResponse<Nothing>()
    object Error : ApiResponse<Nothing>()
    class Success<out T : Any>(val data: T) : ApiResponse<T>()
}