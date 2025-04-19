package com.cornellappdev.transit.ui.components.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Card for each filter on home bottom sheet
 * @param image The icon for the item
 * @param label The label for the item
 * @param isActive Whether the filter is selected
 */
@Composable
fun BottomSheetFilterItem(
    image: Painter,
    label: String,
    isActive: Boolean,
    itemOnClick: () -> Unit,
) {
    val alphaValue: Float by animateFloatAsState(if (isActive) 1f else 0.33f, label = "alpha")

    Column(
        modifier = Modifier
            .width(88.dp)
            .padding(horizontal = 12.dp)
            .clickable(onClick = itemOnClick)
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .width(64.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                image,
                contentDescription = label,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                alpha = alphaValue
            )
        }
        Text(
            label,
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 4.dp),
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight(400),
            fontFamily = robotoFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun PreviewBottomSheetFilterItem() {
    BottomSheetFilterItem(
        image = painterResource(id = R.drawable.eatery_icon),
        label = "Eateries",
        isActive = true
    ) {}

}
