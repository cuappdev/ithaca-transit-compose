package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.ecosystem.OperatingHours
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.Gray05
import com.cornellappdev.transit.ui.theme.Style

/**
 *
 */
@Composable
fun ExpandableOperatingHoursList(annotatedString: AnnotatedString, operatingHours: OperatingHours) {

    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            isExpanded = !isExpanded
        }) {
            Icon(
                painterResource(R.drawable.clock),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp),
                tint = Gray05
            )
            Text(
                text = annotatedString,
                style = Style.detailBody,
                modifier = Modifier.padding(start = 15.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            if (isExpanded) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_expand_less),
                    contentDescription = "Expand less",
                    modifier = Modifier.height(79.dp)
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_expand_more),
                    contentDescription = "Expand more",
                    modifier = Modifier.height(79.dp)
                )
            }
        }

        if(isExpanded) {
            OperatingHoursList(operatingHours)
        }
        HorizontalDivider(thickness = 1.dp, color = DividerGray)
    }

}