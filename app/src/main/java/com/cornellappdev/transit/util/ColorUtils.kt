package com.cornellappdev.transit.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// Source: Uplift Android

/**
 * Interpolates a color between [color1] and [color2] by choosing a color a [fraction] in between.
 * Uses HSV interpolation, which generally gives more aesthetically pleasing results than RGB.
 *
 * @param fraction  Float in [0..1]. 0 = color1, 1 = color2. In between interpolates between.
 */
fun colorInterp(fraction: Float, color1: Color, color2: Color): Color {
    val fractionToUse = fraction.coerceIn(0f, 1f)
    val HSV1 = FloatArray(3)
    val HSV2 = FloatArray(3)
    android.graphics.Color.colorToHSV(color1.toArgb(), HSV1)
    android.graphics.Color.colorToHSV(color2.toArgb(), HSV2)

    for (i in 0..2) {
        HSV2[i] = interpolate(fractionToUse, HSV1[i], HSV2[i])
    }
    return Color.hsv(
        HSV2[0],
        HSV2[1],
        HSV2[2],
        interpolate(fractionToUse, color1.alpha, color2.alpha)
    )
}

/**
 * Interpolates between two floats.
 */
private fun interpolate(fraction: Float, a: Float, b: Float): Float {
    return a + (b - a) * fraction
}