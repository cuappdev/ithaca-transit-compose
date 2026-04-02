package com.cornellappdev.transit.util

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.mutableLongStateOf
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.lifecycle.Lifecycle

private const val NAV_ACTION_DEBOUNCE_MS = 500L

/**
 * Returns a back action that ignores rapid repeat taps and safely falls back home.
 */
@Composable
fun rememberSafeBackAction(
    navController: NavController,
    fallbackRoute: String = "home",
    debounceMs: Long = NAV_ACTION_DEBOUNCE_MS
): () -> Unit {
    val lastBackActionTime = remember { mutableLongStateOf(0L) }

    return {
        val now = SystemClock.elapsedRealtime()
        if (now - lastBackActionTime.longValue >= debounceMs) {
            lastBackActionTime.longValue = now

            if (!navController.popBackStack()) {
                navController.navigateSingleTop(fallbackRoute)
            }
        }
    }
}

/**
 * Returns a forward navigation action that ignores rapid repeat taps and only runs while
 * the current destination is still resumed.
 */
@Composable
fun rememberSafeNavigationAction(
    navController: NavController,
    debounceMs: Long = NAV_ACTION_DEBOUNCE_MS,
    onNavigate: () -> Unit
): () -> Unit {
    val lastNavigationActionTime = remember { mutableLongStateOf(0L) }
    val currentOnNavigate = rememberUpdatedState(onNavigate)

    return {
        val now = SystemClock.elapsedRealtime()
        val isResumed = navController.currentBackStackEntry?.lifecycle?.currentState
            ?.isAtLeast(Lifecycle.State.RESUMED) == true

        if (isResumed && now - lastNavigationActionTime.longValue >= debounceMs) {
            lastNavigationActionTime.longValue = now
            currentOnNavigate.value.invoke()
        }
    }
}

/**
 * Navigates to [route] without creating duplicate destinations on rapid repeat taps.
 */
fun NavController.navigateSingleTop(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route) {
        launchSingleTop = true
        builder()
    }
}



