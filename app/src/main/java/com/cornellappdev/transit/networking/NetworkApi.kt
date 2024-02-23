package com.cornellappdev.transit.networking


import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.StopList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkApi {

    @GET("/api/v1/allStops")
    suspend fun getAllStops(): StopList

}