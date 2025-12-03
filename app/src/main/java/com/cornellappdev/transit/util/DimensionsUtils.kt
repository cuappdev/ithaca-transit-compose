package com.cornellappdev.transit.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Dp.orZeroIfUnspecified(): Dp = if (this == Dp.Unspecified) 0.dp else this