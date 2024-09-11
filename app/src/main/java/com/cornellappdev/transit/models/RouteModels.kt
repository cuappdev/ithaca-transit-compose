package com.cornellappdev.transit.models

import androidx.compose.ui.graphics.Color
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen

/**
 * TODO: add spec
 */

enum class BusLateness {
    LATE {
        override fun color() = LateRed
        override fun text() = "Delayed"
    },
    NORMAL {
        override fun color() = LiveGreen
        override fun text() = "On Time"
    },
    NONE {
        override fun color() = Color.Black
        override fun text() = ""
    };

    abstract fun color(): Color
    abstract fun text(): String
}

/**
 * TODO: add spec
 */
sealed class Transport {
    abstract val startTime: String
    abstract val arriveTime: String
    abstract val lateness: BusLateness

    class WalkOnly(
        override val startTime: String,
        override val arriveTime: String,
        override val lateness: BusLateness = BusLateness.NONE,
        val distance: String,
        val dest: String
    ) : Transport()

    class WalkAndBus(
        override val startTime: String,
        override val arriveTime: String,
        val distance: String,
        val start: String,
        val dest: String,
        val timeToBoard: Int,
        val endStop: String,
        val bus: Int,
        override val lateness: BusLateness
    ) : Transport()

    class BusOnly(
        override val startTime: String,
        override val arriveTime: String,
        val distance: String,
        val start: String,
        val dest: String,
        val transferStop: String,
        val timeToBoard: Int,
        val firstBus: Int,
        val secondBus: Int,
        override val lateness: BusLateness
    ) : Transport()
}