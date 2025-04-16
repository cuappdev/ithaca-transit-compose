package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
fun MemberItem(name: String) {
    Row {
        Icon(
            painter = painterResource(id = R.drawable.small_star),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Card(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(32.dp)
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