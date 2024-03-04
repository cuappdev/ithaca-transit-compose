package com.cornellappdev.transit.networking


import com.cornellappdev.transit.models.RouteRequest
import com.cornellappdev.transit.models.RouteResponse
import com.cornellappdev.transit.models.StopResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkApi {

    @GET("/api/v1/allStops")
    suspend fun getAllStops(): StopResponse

    @POST("/api/v3/route")
    suspend fun getRoute(@Body request: RouteRequest): RouteResponse

    

}