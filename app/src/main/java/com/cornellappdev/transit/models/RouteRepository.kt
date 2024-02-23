package com.cornellappdev.transit.models

import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.networking.NetworkApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for data related to routes
 */
@Singleton

class RouteRepository @Inject constructor(private val networkApi: NetworkApi) {

    suspend fun getAllStops(): StopList = networkApi.getAllStops()


    private val _stopFlow: MutableStateFlow<ApiResponse<StopList>> =
        MutableStateFlow(ApiResponse.Pending)


    init {
        fetchAllStops()
    }

    /**
     * A StateFlow holding a list of all stops
     */
    val stopFlow = _stopFlow.asStateFlow()

    /**
     * Makes a new call to backend for all stops.
     */
    private fun fetchAllStops() {
        _stopFlow.value = ApiResponse.Pending
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val rides = getAllStops()
                _stopFlow.value = ApiResponse.Success(rides)
            } catch (e: Exception) {
                _stopFlow.value = ApiResponse.Error
            }
        }
    }

}