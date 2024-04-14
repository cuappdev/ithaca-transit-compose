package com.cornellappdev.transit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.Stop
import com.cornellappdev.transit.networking.ApiResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.coroutineContext
import kotlinx.coroutines.flow.stateIn

/**
 * Collection of utility functions for searching
 */
object SearchUtils {

    /**
     * True if the stop can be searched via the [query] string
     */
    fun fulfillsQuery(stop: Stop, query: String): Boolean {
        return !query.isBlank() && stop.name.lowercase().contains(query.lowercase())
    }

}

/**
 * Create a Flow of list of stops given a MutableStateFlow [searchQuery] of the searched string
 * and a function [fulfillsQuery] specifying whether a stop fulfills a query string
 *
 * @param searchQuery the MutableStateFlow of a query
 * @param stopFlow the flow of all stops
 * @param fulfillsQuery returns true if the stop p1 is fulfilled by query string p2
 */
fun ViewModel.createStopQueryFlow(
    searchQuery: MutableStateFlow<String>,
    stopFlow: StateFlow<ApiResponse<List<Stop>>>,
    fulfillsQuery: (Stop, String) -> Boolean = SearchUtils::fulfillsQuery,
): StateFlow<List<Stop>> =
    stopFlow.combine(searchQuery) { allStops, filter ->
        when (allStops) {
            is ApiResponse.Error -> {
                emptyList()
            }

            is ApiResponse.Pending -> {
                emptyList()
            }

            is ApiResponse.Success -> {
                allStops.data.filter { stop ->
                    fulfillsQuery(stop, filter)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
