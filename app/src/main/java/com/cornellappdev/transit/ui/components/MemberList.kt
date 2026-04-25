package com.cornellappdev.transit.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.math.abs

/**
 * Compose function to display a list of member items in a horizontal scrolling row.
 * * @param names The list of member names to be displayed.
 * **/
@Composable
fun MemberList(names: List<String>) {
    val listState = rememberLazyListState()
    val isPreview = LocalInspectionMode.current
    val scrollStep = remember(names) {
        val seed = names.joinToString(separator = "|").hashCode()
        80f + (abs(seed) % 120).toFloat()
    }
    val repeatCount = if (isPreview) 1 else 80
    val displayNames = if (names.isEmpty()) emptyList() else List(repeatCount) { names }.flatten()
    val startIndex = if (isPreview || displayNames.isEmpty()) 0 else displayNames.size / 2

    LaunchedEffect(displayNames, isPreview) {
        if (isPreview || displayNames.size <= 1) return@LaunchedEffect

        listState.scrollToItem(startIndex)

        while (isActive) {
            listState.animateScrollBy(
                value = scrollStep,
                animationSpec = tween(durationMillis = 1600, easing = LinearEasing)
            )

            if (listState.firstVisibleItemIndex > displayNames.size - 30) {
                listState.scrollToItem(startIndex)
            }
        }
    }

    LazyRow(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        state = listState,
    ) {
        items(displayNames.size) { index ->
            MemberItem(name = displayNames[index])
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemberListPreview() {
    MemberList(
        names = listOf("Alice", "Bob", "Charlie", "David", "Eve")
    )
}