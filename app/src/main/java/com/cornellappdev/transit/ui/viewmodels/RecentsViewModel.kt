package com.cornellappdev.transit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel handling recents UI state
 */
@HiltViewModel
class RecentsViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    /**
     * A flow emitting all the locations the user has searched recently as a list of stops.
     */
    val recentStops = userPreferenceRepository.recentsFlow

    /**
     * Asynchronous function to add a stop to recents
     */
    fun addRecent(stop: Place?) {
        if (stop != null) {
            var currentRecents = recentStops.value.toMutableList()
            currentRecents = currentRecents
                .filter { it != stop }
                .toMutableList()
            currentRecents.add(0, stop)
            if (currentRecents.size > 5) {
                currentRecents = currentRecents.take(5).toMutableList()
            }
            viewModelScope.launch {
                userPreferenceRepository.setRecents(currentRecents)
            }
        }
    }

    /**
     * Asynchronous function to clear set of recents
     */
    fun clearRecents() {
        viewModelScope.launch {
            userPreferenceRepository.setRecents(mutableListOf())
        }
    }
}