package com.cornellappdev.transit.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable


data class SearchQuery(
    @Json(name = "query") var query: String
)

data class QueryResult(
    @Json(name = "applePlaces") var places: List<Place>,
    @Json(name = "busStops") var stops: List<Place>
)

/**
 * Enum class representing the types of TCAT places
 */
enum class PlaceType {
    @Json(name = "busStop")
    BUS_STOP,

    @Json(name = "applePlace")
    APPLE_PLACE
}

/**
 * Data class representing an apple place or a TCAT stop
 */
@Serializable
data class Place(
    @Json(name = "lat") var latitude: Double,
    @Json(name = "long") var longitude: Double,
    @Json(name = "name") var name: String,
    @Json(name = "detail") val detail: String?,
    @Json(name = "type") var type: PlaceType
) {
    val subLabel
        get() = if (type == PlaceType.BUS_STOP) "Bus Stop" else detail.toString()
}
