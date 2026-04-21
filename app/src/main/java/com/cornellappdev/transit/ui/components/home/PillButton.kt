package com.cornellappdev.transit.ui.components.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun PillButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = R.drawable.ic_addition,
    contentDescription: String = text,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = Color.Black
    ),
    iconTint: Color = SecondaryText,
    textColor: Color = SecondaryText,
    fontFamily: FontFamily = robotoFamily,
    fontWeight: FontWeight = FontWeight.SemiBold,
    fontSize: TextUnit = 16.sp,
) {
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
    ) {
        if (iconResId != null) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = contentDescription,
                tint = iconTint,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = fontSize,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun PillButtonPreview() {
    PillButton(
        onClick = {},
        text = "Add Favorites",
    )
}
