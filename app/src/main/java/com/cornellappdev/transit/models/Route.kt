package com.cornellappdev.transit.models

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json


/**
 * Data class representing a stop in a list of directions
 */
data class DirectionStop(
    @Json(name = "lat") val latitude: String,
    @Json(name = "long") val long: String,
    @Json(name = "name") val name: String,
    @Json(name = "stopId") val stopId: String
)

enum class DirectionType {
    WALK,
    DEPART
}

/**
 * Data class representing a direction
 */
data class Direction(
    @Json(name = "endLocation") val endLocation: LatLng,
    @Json(name = "routeId") val routeId: String?,
    @Json(name = "tripIds") val tripIds: List<String>?,
    @Json(name = "stayOnBusForTransfer") val stayOnBusForTransfer: Boolean?,
    @Json(name = "delay") val delay: Int?,
    @Json(name = "startLocation") val startLocation: LatLng,
    @Json(name = "path") val path: List<LatLng>,
    @Json(name = "type") private val directionType: String, // "walk" or "depart"
    @Json(name = "stops") val stops: List<DirectionStop>,
    @Json(name = "endTime") val endTime: String,
    @Json(name = "distance") val distance: String,
    @Json(name = "startTime") val startTime: String,
) {
    val type
        get() = if (directionType == "walk") DirectionType.WALK else DirectionType.DEPART
}


/**
 * Data class representing routes on the map
 */
data class Route(
    @Json(name = "departureTime") val departureTime: String,
    @Json(name = "arrivalTime") val arrivalTime: String,
    @Json(name = "startName") val startName: String,
    @Json(name = "endName") val endName: String,
    @Json(name = "totalDuration") val totalDuration: Int,
    @Json(name = "travelDistance") val travelDistance: Double,
    @Json(name = "startCoords") val startCoords: LatLng,
    @Json(name = "directions") val directions: List<Direction>,
)

/**
 * Data class wrapping all possible routes from a given start and end
 */
data class RouteOptions(
    @Json(name = "boardingSoon") val boardingSoon: List<Route>?,
    @Json(name = "fromStop") val fromStop: List<Route>?,
    @Json(name = "walking") val walking: List<Route>?
)

/**
 * Data class for parsing a request for a route
 */
data class RouteRequest(
    @Json(name = "end") val end: String,
    @Json(name = "time") val time: Double,
    @Json(name = "destinationName") val destinationName: String,
    @Json(name = "start") val start: String,
    @Json(name = "arriveBy") val arriveBy: Boolean,
    @Json(name = "originName") val originName: String
)