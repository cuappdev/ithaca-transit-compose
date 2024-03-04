package com.cornellappdev.transit.networking


import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteRequest
import com.cornellappdev.transit.models.RouteResponse
import com.cornellappdev.transit.models.StopList
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkApi {

    @GET("/api/v1/allStops")
    suspend fun getAllStops(): StopList

    @POST("/api/v3/route")
    suspend fun getRoute(@Body request: RouteRequest): RouteResponse

    

}