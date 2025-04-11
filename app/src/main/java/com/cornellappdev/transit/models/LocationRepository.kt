package com.cornellappdev.transit.models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.cornellappdev.transit.networking.NetworkApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for current user locations
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

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                _currentLocation.value = locationResult.lastLocation
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5000L //Update location every 5 seconds
            ).build(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

}