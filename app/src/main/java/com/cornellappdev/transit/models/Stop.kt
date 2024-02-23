package com.cornellappdev.transit.models

import com.squareup.moshi.Json

/**
 * List of all stops returned from backend
 */
data class StopList(
    @Json(name = "success") var success: Boolean,
    @Json(name = "data") var stops: List<Stop>
)

/**
 * Data class representing a TCAT stop
 */
data class Stop(
    @Json(name = "lat") var latitude: Double,
    @Json(name = "long") var longitude: Double,
    @Json(name = "name") var name: String,
    @Json(name = "type") var type: String
)
