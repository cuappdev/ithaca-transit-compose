package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.DetailsHeaderGray
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.FavoritesFilterSheetState

@Composable
fun FavoritesFilterBottomSheet(
    onCancelClicked: () -> Unit,
    onApplyClicked: () -> Unit,
    filters: List<FavoritesFilterSheetState>,
    selectedFilters: Set<FavoritesFilterSheetState>,
    onFilterToggle: (FavoritesFilterSheetState) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = DetailsHeaderGray)
            .padding(top = 24.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Location Type",
                fontFamily = robotoFamily,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = SecondaryText
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filters) {
                FavoritesFilterSheetItem(
                    iconId = it.iconId,
                    label = it.label,
                    isActive = it in selectedFilters,
                    itemOnClick = { onFilterToggle(it) }
                )
            }
        }

        // Footer with Cancel and Apply buttons
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterButton(
                onClick = onCancelClicked,
                text = "Cancel",
                isCancel = true,
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FooterButton(onClick = onApplyClicked, text = "Apply", modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
private fun FooterButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isCancel: Boolean = false
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isCancel) Color.White else TransitBlue,
            contentColor = if (isCancel) TransitBlue else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (isCancel) BorderStroke(1.dp, TransitBlue) else null,
        modifier = modifier.height(40.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(letterSpacing = 0.sp),
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesFilterBottomSheetPreview() {
    FavoritesFilterBottomSheet(
        onCancelClicked = {},
        onApplyClicked = {},
        filters = listOf(
            FavoritesFilterSheetState.EATERIES,
            FavoritesFilterSheetState.OTHER,
            FavoritesFilterSheetState.LIBRARIES,
            FavoritesFilterSheetState.PRINTERS,
            FavoritesFilterSheetState.GYMS
        ),
        selectedFilters = setOf(
            FavoritesFilterSheetState.EATERIES,
            FavoritesFilterSheetState.LIBRARIES
        ),
        onFilterToggle = {}
    )
}
