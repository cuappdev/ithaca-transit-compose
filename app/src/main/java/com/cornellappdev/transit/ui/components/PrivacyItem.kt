package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun PrivacyItem(text: String, subtext: String, onclick: () -> Unit, icon: ImageVector) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .clickable { onclick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp),
                fontFamily = robotoFamily,
                fontStyle = FontStyle.Normal,
            )
            if (subtext.isNotEmpty()) {
                Text(
                    text = subtext,
                    fontSize = 12.sp,
                    fontFamily = robotoFamily,
                    modifier = Modifier.padding(start = 16.dp),
                    fontStyle = FontStyle.Normal,
                    color = Color.Gray
                )
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(icon, "", tint = TransitBlue)
    }
}