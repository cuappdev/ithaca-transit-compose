package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.FavoritesFilterSheetState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterRow(
    selectedFilters: Set<FavoritesFilterSheetState>,
    onFilterClick: () -> Unit,
    onRemoveFilter: (FavoritesFilterSheetState) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        FilterButton(
            onFilterClick = onFilterClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        // Selected filter labels
        if (selectedFilters.isNotEmpty()) {
            selectedFilters.forEach { filter ->
                FilterLabel(
                    text = filter.label,
                    onRemove = { onRemoveFilter(filter) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
private fun FilterLabel(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(1.dp, TransitBlue),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, top = 10.dp, bottom = 10.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = robotoFamily,
                color = TransitBlue
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    tint = TransitBlue,
                    contentDescription = "Remove filter",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun FilterRowPreview() {
    FilterRow(
        selectedFilters = setOf(FavoritesFilterSheetState.EATERIES),
        onFilterClick = {},
        onRemoveFilter = {}
    )

}

@Preview(showBackground = true)
@Composable
private fun FilterLabelPreview() {
    FilterLabel("Eateries", {})
}