package com.cornellappdev.transit.ui.viewmodels.state

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.SearchQuery
import com.cornellappdev.transit.networking.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SearchBarUIState(
    val searchQuery: StateFlow<String> = MutableStateFlow(""),
    val searched: StateFlow<ApiResponse<List<Place>>> =  MutableStateFlow(ApiResponse.Pending),
    val recents: StateFlow<List<Place>> = MutableStateFlow(emptyList()),
    val favorites: StateFlow<Set<Place>> = MutableStateFlow(emptySet()),
)