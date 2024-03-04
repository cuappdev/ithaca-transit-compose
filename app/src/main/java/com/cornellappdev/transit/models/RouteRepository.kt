package com.cornellappdev.transit.models

import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.networking.NetworkApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for data related to routes
 */
@Singleton

class RouteRepository @Inject constructor(private val networkApi: NetworkApi) {

    suspend fun getAllStops(): StopResponse = networkApi.getAllStops()

    private suspend fun getRoute(request: RouteRequest): RouteResponse =
        networkApi.getRoute(request)


    private val _stopFlow: MutableStateFlow<ApiResponse<List<Stop>>> =
        MutableStateFlow(ApiResponse.Pending)

    private val _lastRouteFlow: MutableStateFlow<ApiResponse<RouteOptions>> =
        MutableStateFlow(ApiResponse.Pending)

    init {
        fetchAllStops()
    }

    /**
     * A StateFlow holding a list of all stops
     */
    val stopFlow = _stopFlow.asStateFlow()

    /**
     * A StateFlow holding the last queried route
     */
    val lastRouteFlow = _lastRouteFlow.asStateFlow()

    /**
     * Makes a new call to backend for all stops.
     */
    private fun fetchAllStops() {
        _stopFlow.value = ApiResponse.Pending
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val rides = getAllStops()
                _stopFlow.value = ApiResponse.Success(rides.data)
            } catch (e: Exception) {
                _stopFlow.value = ApiResponse.Error
            }
        }
    }

    /**
     * Retrieve a route between [start] and [end] from backend
     *
     * @param end The latitude and longitude of the destination
     * @param time The time of the route request
     * @param destinationName The name of the destination
     * @param start The latitude and longitude of the origin
     * @param arriveBy Whether the route must complete by a certain time
     * @param originName The name of the origin
     */
    fun fetchRoute(
        end: LatLng,
        time: Double,
        destinationName: String,
        start: LatLng,
        arriveBy: Boolean,
        originName: String
    ) {
        _lastRouteFlow.value = ApiResponse.Pending
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val routeResponse = getRoute(
                    RouteRequest(
                        end = "${end.latitude}, ${end.longitude}",
                        time = time,
                        destinationName = destinationName,
                        start = "${start.latitude}, ${start.longitude}",
                        arriveBy = arriveBy,
                        originName = originName
                    )
                )
                _lastRouteFlow.value = ApiResponse.Success(routeResponse.data)
            } catch (e: Exception) {
                _lastRouteFlow.value = ApiResponse.Error
            }
        }
    }

}