package com.cornellappdev.transit.ui.components.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.models.ecosystem.UpliftCapacity
import com.cornellappdev.transit.ui.theme.AccentClosed
import com.cornellappdev.transit.ui.theme.AccentOpen
import com.cornellappdev.transit.ui.theme.AccentOrange
import com.cornellappdev.transit.ui.theme.Gray02
import com.cornellappdev.transit.ui.theme.Gray04
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.util.LIMITED_CAPACITY_THRESHOLD
import com.cornellappdev.transit.util.colorInterp

// Source: Uplift Android

/**
 * A circular indicator for the capacity at a given gym.
 *
 * @param capacity  A tuple whose first element is the current number of people at the gym
 *                  and whose second element is the max capacity.
 * @param label     The name of the gym placed under this indicator. Can be null to indicate no
 *                  label.
 * @param closed    If this gym is closed.
 */
@Composable
fun GymCapacityIndicator(
    capacity: UpliftCapacity?,
    label: String?,
    closed: Boolean,
) {
    val grayedOut = closed || capacity == null

    val fraction = if (!grayedOut) capacity!!.percent.toFloat() else 0f
    val animatedFraction = remember { Animatable(0f) }

    // When the composable launches, animate the fraction to the capacity fraction.
    LaunchedEffect(animatedFraction) {
        animatedFraction.animateTo(
            fraction,
            animationSpec = tween(durationMillis = 750)
        )
    }

    // Choose a color. If between 0 & 0.5, tween between open and orange. If between 0.5 and 1,
    // tween between orange and closed.
    val color =
        if (fraction > LIMITED_CAPACITY_THRESHOLD)
            colorInterp(
                (fraction - LIMITED_CAPACITY_THRESHOLD) / (1 - LIMITED_CAPACITY_THRESHOLD),
                AccentOrange,
                AccentClosed
            )
        else
            colorInterp(
                fraction / LIMITED_CAPACITY_THRESHOLD,
                AccentOpen,
                AccentClosed
            )

    val size = 54.dp
    val percentFontSize =
        12.sp * (if (closed || capacity != null) 1f else .9f)
    val labelColor = PrimaryText
    val labelFontWeight = FontWeight(600)
    val labelPadding = 12.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            CircularProgressIndicator(
                color = Gray02,
                strokeWidth = size / 9,
                modifier = Modifier.size(size),
                progress = 1f
            )
            CircularProgressIndicator(
                color = color,
                strokeWidth = size / 9,
                modifier = Modifier.size(size),
                progress = animatedFraction.value,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = if (closed) "CLOSED"
                else capacity?.percentString() ?: "NO DATA",
                fontFamily = robotoFamily,
                fontSize = percentFontSize,
                fontWeight = FontWeight(700),
                color = if (grayedOut) Gray02 else PrimaryText,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
        if (label != null) {
            Spacer(modifier = Modifier.height(labelPadding))
            Text(
                text = label,
                fontFamily = robotoFamily,
                fontSize = 14.sp,
                fontWeight = labelFontWeight,
                color = labelColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}