package com.cornellappdev.transit.models

import com.squareup.moshi.Json

/**
 * Bus location for the bus on route [routeId]
 */
data class BusLocation(
    @Json(name = "latitude") var latitude: Double,
    @Json(name = "longitude") var longitude: Double,
    @Json(name = "routeId") var routeId: String,
    @Json(name = "vehicleId") var vehicleId: String
)

/**
 * List of routes/trips that we want to make a tracking request for
 */
data class TrackingRequestList(
    @Json(name = "data") var requestList: List<TrackingRequest>,
)

/**
 * Route and trip that we want to make a tracking request for
 */
data class TrackingRequest(
    @Json(name = "routeId") var routeId: String,
    @Json(name = "tripId") var tripId: String
)