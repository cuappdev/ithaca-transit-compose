package com.cornellappdev.transit.networking


import com.cornellappdev.transit.models.BusLocation
import com.cornellappdev.transit.models.DelayInfo
import com.cornellappdev.transit.models.DelayRequest
import com.cornellappdev.transit.models.DelayRequestList
import com.cornellappdev.transit.models.Payload
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.models.RouteRequest
import com.cornellappdev.transit.models.QueryResult
import com.cornellappdev.transit.models.SearchQuery
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.TrackingRequest
import com.cornellappdev.transit.models.TrackingRequestList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkApi {

    @GET("/api/v1/allStops")
    suspend fun getAllStops(): Payload<List<Place>>

    @POST("/api/v3/appleSearch")
    suspend fun appleSearch(@Body request: SearchQuery): Payload<QueryResult>

    @POST("/api/v3/route")
    suspend fun getRoute(@Body request: RouteRequest): Payload<RouteOptions>

    @POST("/api/v3/tracking")
    suspend fun getTracking(@Body request: TrackingRequestList): Payload<BusLocation>

    @POST("/api/v3/delays")
    suspend fun getDelay(@Body request: DelayRequestList): Payload<DelayInfo>

}