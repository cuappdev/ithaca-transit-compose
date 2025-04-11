package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R


@Composable
fun MemberList(team: String, names: List<String>) {
    LazyRow(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Text(
                text = team,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }
        items(names) {
            MemberItem(name = it)
        }
    }
}

@Composable
fun MemberItem(name: String) {
    Row {
        Icon(
            painter = painterResource(id = R.drawable.small_star),
            contentDescription = null,
            tint = Color(0xFFEFF1F4),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(8.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFB3E5FC), Color(0xFFE1F5FE))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = name,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMemberItem() {
    MemberItem(name = "Alice")
}

@Preview(showBackground = true)
@Composable
private fun PreviewMemberList() {
    MemberList(
        team = "Design",
        names = listOf("Alice", "Bob", "Charlie", "David", "Eve")
    )
}