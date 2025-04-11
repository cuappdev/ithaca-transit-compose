package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun SwitchItem(text: String, subtext: String, onclick: () -> Unit) {
    var isChecked = false
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = text,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = robotoFamily,
                fontStyle = FontStyle.Normal,
            )
            if (subtext.isNotEmpty()) {
                Text(
                    text = subtext,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp),
                    fontFamily = robotoFamily,
                    fontStyle = FontStyle.Normal,
                )
            }

        }
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFEFF1F4),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFEFF1F4)
            ),
            modifier = Modifier
                .padding(8.dp)
        )
    }

}