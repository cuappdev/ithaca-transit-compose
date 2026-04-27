package com.cornellappdev.transit.util

import com.cornellappdev.transit.BuildConfig

const val ECOSYSTEM_FLAG = BuildConfig.ECOSYSTEM_FLAG

const val BOTTOM_SHEET_MAX_HEIGHT_PERCENT = 90

/** When the capacity turns to orange */
const val MEDIUM_CAPACITY_THRESHOLD = 0.35f

/** When the capacity turns to red */
const val HIGH_CAPACITY_THRESHOLD = 0.65f

const val NOTIFICATIONS_ENABLED = false

const val METERS_TO_FEET = 3.28084

const val LEAVE_AT_MAX_DISPLAYED_ROUTES = 3

const val LEAVE_CUTOFF_HORIZON_MINUTES = 45L

const val LEAVE_CUTOFF_GRACE_MINUTES = 2L

const val ARRIVE_BY_CUTOFF_GRACE_MINUTES = 2L

// Hide transit options when walking arrives at the same time or sooner (+ tie buffer).
const val WALKING_TRANSIT_TIE_MINUTES = 1L

