package com.cornellappdev.transit.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cornellappdev.transit.models.RouteRepository
import com.google.android.gms.maps.model.LatLng


/**
 * ViewModel handling home screen UI state and search functionality
 */
class HomeViewModel(
) : ViewModel() {

    //TODO: Replace with Flow from backend, this is a placeholder
    val placeData = MutableList(100) { "Gates Hall" }

    //Default map location
    val defaultIthaca = LatLng(42.44, -76.50)

    /**
     * The current query in the search bar
     */
    val searchQuery = mutableStateOf("")

    /**
     * Perform a search on the string [query]
     */
    fun onSearch(query: String) {
        Log.d("SEARCHED", query)
    }

    /**
     * Change the query in the search bar
     */
    fun onQueryChange(query: String) {
        searchQuery.value = query;
    }

}