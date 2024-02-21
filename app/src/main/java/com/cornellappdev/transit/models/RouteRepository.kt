package com.cornellappdev.transit.models

import com.cornellappdev.transit.networking.NetworkApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for data related to routes
 */
@Singleton

class RouteRepository @Inject constructor(private val networkApi: NetworkApi){

    suspend fun getAllStops(): StopList = networkApi.getAllStops()

}