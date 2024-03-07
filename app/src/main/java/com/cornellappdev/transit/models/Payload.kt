package com.cornellappdev.transit.models

import com.squareup.moshi.Json

/**
 * Wrapper class to flatten Transit backend calls
 */
data class Payload<T>(
    @Json(name = "success") var success: Boolean,
    @Json(name = "data") var data: T
) {
    /**
     * Unwrap the [data] in the response object. Fail-fast check on [success]
     */
    fun unwrap() : T {
        if (!success){
            throw Exception("Response unwrap failed: Success is false")
        }
        return data
    }
}