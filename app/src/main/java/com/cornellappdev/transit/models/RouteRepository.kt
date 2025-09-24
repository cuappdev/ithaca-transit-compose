package com.cornellappdev.transit.models

import android.util.Log
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.networking.EcosystemNetworkApi
import com.cornellappdev.transit.networking.NetworkApi
import com.cornellappdev.transit.util.ECOSYSTEM_FLAG
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
class RouteRepository @Inject constructor(
    private val networkApi: NetworkApi,
    private val ecosystemNetworkApi: EcosystemNetworkApi
) {

    private suspend fun getAllStops(): Payload<List<Place>> = networkApi.getAllStops()

    private suspend fun appleSearch(query: SearchQuery): Payload<QueryResult> {
        if (ECOSYSTEM_FLAG) {
            return networkApi.v2AppleSearch(query)
        } else {
            return networkApi.v3AppleSearch(query)
        }
    }

    private suspend fun getRoute(request: RouteRequest): Payload<RouteOptions> =
        networkApi.getRoute(request)

    private suspend fun getTracking(request: TrackingRequestList): Payload<BusLocation> =
        networkApi.getTracking(request)

    private suspend fun getDelay(request: DelayRequestList): Payload<DelayInfo> =
        networkApi.getDelay(request)

    private suspend fun getPrinters(): Payload<List<Printer>> =
        ecosystemNetworkApi.getPrinters()

    private suspend fun getLibraries(): Payload<List<Library>> =
        ecosystemNetworkApi.getLibraries()

    private val _stopFlow: MutableStateFlow<ApiResponse<List<Place>>> =
        MutableStateFlow(ApiResponse.Pending)

    private val _placeFlow: MutableStateFlow<ApiResponse<List<Place>>> =
        MutableStateFlow(ApiResponse.Pending)

    private val _lastRouteFlow: MutableStateFlow<ApiResponse<RouteOptions>> =
        MutableStateFlow(ApiResponse.Pending)

    private val _printerFlow: MutableStateFlow<ApiResponse<List<Printer>>> =
        MutableStateFlow(ApiResponse.Pending)

    private val _libraryFlow: MutableStateFlow<ApiResponse<List<Library>>> =
        MutableStateFlow(ApiResponse.Pending)

    init {
        fetchAllStops()
        if (ECOSYSTEM_FLAG) {
            fetchAllPrinters()
            fetchAllLibraries()
        }
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
     * A StateFlow holding the last queried location
     */
    val placeFlow = _placeFlow.asStateFlow()

    /**
     * A StateFlow holding the list of all printers
     */
    val printerFlow = _printerFlow.asStateFlow()

    /**
     * A StateFlow holding the list of all libraries
     */
    val libraryFlow = _libraryFlow.asStateFlow()

    /**
     * Makes a new call to backend for all stops.
     */
    private fun fetchAllStops() {
        _stopFlow.value = ApiResponse.Pending
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val rideResponse = getAllStops()
                _stopFlow.value = ApiResponse.Success(rideResponse.unwrap())
            } catch (e: Exception) {
                _stopFlow.value = ApiResponse.Error
            }
        }
    }

    /**
     * Makes a new call to backend for all printers
     */
    private fun fetchAllPrinters() {
        _printerFlow.value = ApiResponse.Pending
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val printerResponse = getPrinters()
                _printerFlow.value = ApiResponse.Success(printerResponse.unwrap())
            } catch (e: Exception) {
                _printerFlow.value = ApiResponse.Error
            }
        }
    }

    /**
     * Makes a new call to backend for all libraries
     */
    private fun fetchAllLibraries() {
        _libraryFlow.value = ApiResponse.Pending
        Log.d("LIBRARIES", "LIBRARY)")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val libraryResponse = getLibraries()
                _libraryFlow.value = ApiResponse.Success(libraryResponse.unwrap())
            } catch (e: Exception) {
                _libraryFlow.value = ApiResponse.Error
            }
        }
    }

    /**
     * Makes a new call to places related to a query string.
     */
    fun makeSearch(query: String) {
        _placeFlow.value = ApiResponse.Pending
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val placeResponse = appleSearch(SearchQuery(query))
                val res = placeResponse.unwrap()
                val totalLocations = (res.places ?: emptyList()) + (res.stops ?: (emptyList()))
                _placeFlow.value = ApiResponse.Success(totalLocations)
            } catch (e: Exception) {
                _placeFlow.value = ApiResponse.Error
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
                _lastRouteFlow.value = ApiResponse.Success(routeResponse.unwrap())
            } catch (e: Exception) {
                _lastRouteFlow.value = ApiResponse.Error
            }
        }
    }

}