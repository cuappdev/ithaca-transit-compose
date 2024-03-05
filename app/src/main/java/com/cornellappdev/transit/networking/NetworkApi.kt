package com.cornellappdev.transit.networking


import com.cornellappdev.transit.models.Payload
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.models.RouteRequest
import com.cornellappdev.transit.models.Stop
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkApi {

    @GET("/api/v1/allStops")
    suspend fun getAllStops(): Payload<List<Stop>>

    @POST("/api/v3/route")
    suspend fun getRoute(@Body request: RouteRequest): Payload<RouteOptions>

    

}