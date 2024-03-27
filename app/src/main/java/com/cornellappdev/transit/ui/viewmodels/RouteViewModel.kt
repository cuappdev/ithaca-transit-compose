package com.cornellappdev.transit.ui.viewmodels

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.Stop
import com.cornellappdev.transit.networking.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    /**
     * Callback for entering searches
     */
    fun onSearch(query: String) {

    }

}

