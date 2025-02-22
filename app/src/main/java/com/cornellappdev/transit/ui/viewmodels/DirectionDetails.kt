package com.cornellappdev.transit.ui.viewmodels

import com.cornellappdev.transit.models.DirectionStop
import com.cornellappdev.transit.models.DirectionType

data class DirectionDetails(
    val startTime: String,
    val endTime: String,
    val movementDescription: String,
    val destination: String,
    val directionType: DirectionType,
    val busNumber: String,
    val numStops: Int = 0,
    val duration: Int = 0,
    val stops: List<DirectionStop>,
    val busTransfer: Boolean,
)