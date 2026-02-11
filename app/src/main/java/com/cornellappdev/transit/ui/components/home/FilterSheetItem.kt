package com.cornellappdev.transit.ui.components.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Card for each filter on home bottom sheet
 * @param image The icon for the item
 * @param label The label for the item
 * @param isActive Whether the filter is selected
 */
@Composable
fun FavoritesFilterSheetItem(
    @DrawableRes imageResId: Int,
    label: String,
    isActive: Boolean,
    itemOnClick: () -> Unit,
) {
    val alphaValue: Float by animateFloatAsState(if (isActive) 1f else 0.33f, label = "alpha")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .border(
                width = 1.dp,
                color = if(isActive) TransitBlue else MetadataGray,
                shape = RoundedCornerShape(8.dp)
            )
            //TODO: Fix background color specifics
            .background(color = if(isActive) TransitBlue.copy(alpha = 0.1f) else Color.Transparent, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = itemOnClick),
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .width(64.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painterResource(id = imageResId),
                contentDescription = label,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                alpha = alphaValue
            )
        }
        //TODO: Review Text styles once icons are imported correctly
        Text(
            label,
            color = if(isActive) TransitBlue else MetadataGray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 4.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = robotoFamily,
            style = TextStyle(letterSpacing = 0.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFavoritesFilterSheetItemActive() {
    FavoritesFilterSheetItem(
        imageResId = R.drawable.eatery_icon,
        label = "Eateries",
        isActive = true
    ) {}

}

@Preview(showBackground = true)
@Composable
private fun InactiveFavoritesFilterSheetItemInactive() {
    FavoritesFilterSheetItem(
        imageResId = R.drawable.eatery_icon,
        label = "Eateries",
        isActive = false
    ) {}

}
