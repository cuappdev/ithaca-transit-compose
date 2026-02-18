package com.cornellappdev.transit.util

import com.cornellappdev.transit.models.ecosystem.DayOperatingHours

internal val sampleHours = listOf(
    DayOperatingHours("Tuesday", listOf("10:00 AM - 10:00 PM")),
    DayOperatingHours("Wednesday", listOf("10:00 AM - 5:00 PM")),
    DayOperatingHours("Thursday", listOf("Closed")),
    DayOperatingHours("Friday", listOf("10:00 AM - 5:00 PM")),
    DayOperatingHours(
        "Saturday", listOf(
            "8:00 AM - 9:30 AM",
            "10:30 AM - 2:00 PM",
            "5:00 PM - 8:00 PM"
        )
    ),
    DayOperatingHours(
        "Sunday", listOf(
            "10:00 AM - 2:00 PM",
            "5:00 PM - 8:30 PM"
        )
    ),
    DayOperatingHours("Monday", listOf("10:00 AM - 10:00 PM")),
)