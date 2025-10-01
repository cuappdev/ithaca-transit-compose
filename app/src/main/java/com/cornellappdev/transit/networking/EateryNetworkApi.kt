package com.cornellappdev.transit.networking

import com.cornellappdev.transit.models.ecosystem.Eatery
import retrofit2.http.GET

interface EateryNetworkApi {
    @GET("/eatery/simple")
    suspend fun getEateries(): List<Eatery>

}