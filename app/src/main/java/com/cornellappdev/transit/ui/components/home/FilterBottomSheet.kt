package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.FavoritesFilterSheetState

@Composable
fun FilterBottomSheet(
    onCancelClicked: () -> Unit,
    onApplyClicked: () -> Unit,
    filters: List<FavoritesFilterSheetState>,
    selectedFilters: Set<FavoritesFilterSheetState>,
    onFilterToggle: (FavoritesFilterSheetState) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
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
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filters) {
                FilterSheetItem(
                    imageResId = it.iconId,
                    label = it.label,
                    isActive = it in selectedFilters,
                    itemOnClick = { onFilterToggle(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Footer with Cancel and Apply buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterButton(onClick = onCancelClicked, text = "Cancel", modifier = Modifier.weight(0.5f))
            Spacer(modifier = Modifier.width(16.dp))
            FooterButton(onClick = onApplyClicked, text = "Apply", modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
private fun FooterButton(
    onClick : () -> Unit,
    text: String,
    modifier : Modifier = Modifier
){
    val isCancel = text == "Cancel"
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if(isCancel) Color.White else TransitBlue,
            contentColor = if(isCancel) TransitBlue else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = if(isCancel) BorderStroke(1.dp, TransitBlue) else null,
        modifier = modifier.height(40.dp)) {
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