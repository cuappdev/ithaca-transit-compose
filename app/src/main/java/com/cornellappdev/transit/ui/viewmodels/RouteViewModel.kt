package com.cornellappdev.transit.ui.viewmodels

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import com.cornellappdev.transit.models.RouteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalMaterial3Api::class)
class RouteViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
) : ViewModel() {
    //TODO replace these with flow/actual data values
    val startPl = "Current Location"

    val destPl = "Destination"

    val time = "12:00AM"

    // Route select sheet
    /**
     * The current query in the search bar, as a StateFlow
     */
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Flow of all TCAT stops
     */
    val stopFlow = routeRepository.stopFlow

    /**
     * Search query filtered flow of all TCAT stops
     */
    val queryFlow =
        createStopQueryFlow(searchQuery, stopFlow)

    /**
     * Change the query in the search bar and update search results
     */
    fun onQueryChange(query: String) {
        searchQuery.value = query;
    }

}

