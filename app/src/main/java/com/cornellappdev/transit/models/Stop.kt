package com.cornellappdev.transit.models

import com.squareup.moshi.Json

/**
 * Enum class representing the types of TCAT places
 */
enum class Type() {
    busStop,
    applePlace
}

/**
 * Data class representing a TCAT stop
 */
data class Stop(
    @Json(name = "lat") var latitude: Double,
    @Json(name = "long") var longitude: Double,
    @Json(name = "name") var name: String,
    @Json(name = "detail") val detail: String?,
    @Json(name = "type") var type: Type

)
