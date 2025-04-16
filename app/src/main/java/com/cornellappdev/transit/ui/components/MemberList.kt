package com.cornellappdev.transit.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import kotlinx.coroutines.launch


@Composable
fun MemberList(team: String, names: List<String>) {
    var scrolled by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenDensity = configuration.densityDpi / 160f
    val totalItems = 1000
    val itemsPerGroup = 1 + names.size
    val totalVisibleItems = totalItems * itemsPerGroup

    LaunchedEffect(Unit) {
        listState.scrollToItem(totalVisibleItems / 2)
    }

    LazyRow(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        state = listState,
    ) {
        if (!scrolled) {
            val screenWidth = configuration.screenWidthDp.toFloat() * screenDensity
            val scrollDist = ((screenWidth / 2 + (Math.random() * screenWidth)) * .65).toFloat()
            val multFactor = 50000
            coroutineScope.launch {
                listState.animateScrollBy(5000.0f, snap(0))
                scrolled = true
                listState.animateScrollBy(
                    scrollDist * multFactor,
                    tween(5000 * multFactor, 0, LinearEasing)
                )
            }
        }
        items(totalVisibleItems) { index ->
            val posInGroup = index % itemsPerGroup
            if (posInGroup == 0) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.small_star),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = team,
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                }
            } else {
                val memberIndex = (posInGroup - 1) % names.size
                MemberItem(name = names[memberIndex])
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMemberList() {
    MemberList(
        team = "Design",
        names = listOf("Alice", "Bob", "Charlie", "David", "Eve")
    )
}