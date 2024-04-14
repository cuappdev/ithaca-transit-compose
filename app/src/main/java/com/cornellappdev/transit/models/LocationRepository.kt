package com.cornellappdev.transit.models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.networking.NetworkApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
class LocationRepository @Inject constructor(private val networkApi: NetworkApi) {

    //Source: Uplift

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val _currentLocation: MutableStateFlow<Location?> = MutableStateFlow(null)

    /**
     * Either emits the current user's location, or null if the location has not yet
     * been initialized.
     * */
    var currentLocation = _currentLocation.asStateFlow()

    /**
     * Starts updating [currentLocation] to the user's current location.
     */
    fun instantiate(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        updateLocation(context)
    }


    private fun updateLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            _currentLocation.value = it
        }
    }

}