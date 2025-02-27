package com.cornellappdev.transit.models

import com.squareup.moshi.Json

/**
 * Delay information for a particular stop on a trip
 */
data class DelayInfo(
    @Json(name = "tripId") var tripId: String,
    @Json(name = "stopId") var stopId: String,
    @Json(name = "delay") var delay: Int
)

/**
 * List of stops/trips that we want to make a delay request for
 */
data class DelayRequestList(
    @Json(name = "data") var requestList: List<DelayRequest>,
)

/**
 * The stop on a trip in which we want to make a delay request for
 */
data class DelayRequest(
    @Json(name = "stopId") var stopId: String,
    @Json(name = "tripId") var tripId: String
)