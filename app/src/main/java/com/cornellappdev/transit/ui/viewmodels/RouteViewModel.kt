
package com.cornellappdev.transit.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val repository: RouteRepository,
) : ViewModel() {
    //TODO replace these with flow/actual data values
    val startPl = "Current Location"

    val destPl = "Destination"

    val time = "12:00AM"
}

