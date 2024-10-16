package com.cornellappdev.transit.ui.viewmodels

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.networking.ApiResponse
import kotlinx.coroutines.flow.StateFlow

/**
 * Wrapper for state of the search bar in HomeScreen
 */
sealed class SearchBarUIState {
    data class RecentAndFavorites(val favorites: Set<Place>, val recents: List<Place>) : SearchBarUIState()
    data class Query(val searched: ApiResponse<List<Place>>, val queryText: String) : SearchBarUIState()
}