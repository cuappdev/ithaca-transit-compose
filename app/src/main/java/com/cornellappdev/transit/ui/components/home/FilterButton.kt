package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun FilterButton(
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onFilterClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = SecondaryText
        ),
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.filter_icon),
            contentDescription = "Filter Icon",
            modifier = modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "Filter",
            fontSize = 16.sp,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreview() {
    FilterButton(onFilterClick = {})
}