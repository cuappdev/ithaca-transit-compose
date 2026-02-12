package com.cornellappdev.transit.ui.components.home

import android.R.attr.text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun AddFavoritesButton(
    onAddFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onAddFavoritesClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_addition),
            contentDescription = "Add Favorite",
            tint = SecondaryText,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Add Favorites",
            fontFamily = robotoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = SecondaryText
        )
    }
}

@Preview
@Composable
private fun AddFavoritesButtonPreview() {
    AddFavoritesButton(
        onAddFavoritesClick = {},
    )
}