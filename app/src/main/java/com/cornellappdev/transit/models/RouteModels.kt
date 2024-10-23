package com.cornellappdev.transit.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.util.TimeUtils

/**
 * Enum representing whether a bus is late
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
 * Class representing a total path for a route
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

/**
 * Create a Transport object from Route
 */
@RequiresApi(Build.VERSION_CODES.O)
fun createTransport(route: Route) : Transport {
    // TODO: We need to change the Transport class to accurately reflect all possible route configs
    val allBus = route.directions.all { dir ->
        dir.type == "depart"
    }
    val containsBus = route.directions.any { dir ->
        dir.type == "depart"
    }

    if (allBus || containsBus) {
        return Transport.BusOnly(
            startTime = TimeUtils.getHHMM(route.departureTime),
            arriveTime = TimeUtils.getHHMM(route.arrivalTime),
            distance = String.format("%.1f", route.travelDistance),
            start = route.startName,
            dest = route.endName,
            firstBus = route.directions[0].routeId?.toInt() ?: 0,
            lateness = BusLateness.LATE,
            secondBus = route.directions[1].routeId?.toInt() ?: 0,
            timeToBoard = 0,
            transferStop = ""

        )
    } else {
        return Transport.WalkOnly(
            startTime = TimeUtils.getHHMM(route.departureTime),
            arriveTime = TimeUtils.getHHMM(route.arrivalTime),
            dest = route.endName,
            distance = String.format("%.1f", route.travelDistance)
        )
    }

}