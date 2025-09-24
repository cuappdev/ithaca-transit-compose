package com.cornellappdev.transit.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Compose function to display a list of member items in a horizontal scrolling row.
 * * @param names The list of member names to be displayed.
 * **/
@Composable
fun MemberList(names: List<String>) {
    var scrolled by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Get the screen density and calculate the scroll distance
    val configuration = LocalConfiguration.current
    val screenDensity = configuration.densityDpi / 160f
    val screenWidth = configuration.screenWidthDp.toFloat() * screenDensity
    val scrollDist = ((screenWidth / 2 + (Math.random() * screenWidth)) * .65).toFloat() * 50000

    // Calculate the total number of items to be displayed to simulate infinite scrolling
    val nameRepeatCount = 1000
    val totalItems = nameRepeatCount * names.size

    // Scroll to the middle of the list on first composition
    LaunchedEffect(Unit) {
        listState.scrollToItem(totalItems / 2)
    }

    LazyRow(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        state = listState,
    ) {
        // Trigger animated scroll only once
        if (!scrolled) {
            coroutineScope.launch {
                scrolled = true
                listState.animateScrollBy(
                    scrollDist,
                    tween(250000000, 0, LinearEasing)
                )
            }
        }
        items(totalItems) { index ->
            val memberIndex = index % names.size
            MemberItem(name = names[memberIndex])
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMemberList() {
    MemberList(
        names = listOf("Alice", "Bob", "Charlie", "David", "Eve")
    )
}