package com.cornellappdev.transit.models

import com.squareup.moshi.Json


data class SearchQuery(
    @Json(name = "query") var query : String
)

data class QueryResult(
    @Json(name = "applePlaces") var places : List<Place>,
    @Json(name = "busStops") var stops : List<Place>
)

@Deprecated("Migrate to Place")
data class Stop(
    @Json(name = "lat") var latitude: Double,
    @Json(name = "long") var longitude: Double,
    @Json(name = "name") var name: String,
    @Json(name = "type") var type: String,
)

/**
 * Data class representing an apple place or TCAT stop
 */
data class Place(
    @Json(name = "lat") var latitude: Double,
    @Json(name = "long") var longitude: Double,
    @Json(name = "name") var name: String,
    @Json(name = "type") var type: String,
    @Json(name = "detail") var detail: String?
)
