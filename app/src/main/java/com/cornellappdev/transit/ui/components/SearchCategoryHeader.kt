package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily

/**
 * Headers for suggested searches
 */
@Composable
fun SearchCategoryHeader(headerText: String, buttonText: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = headerText,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = sfProDisplayFamily,
            color = MetadataGray,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = buttonText,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = sfProDisplayFamily,
            color = TransitBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}