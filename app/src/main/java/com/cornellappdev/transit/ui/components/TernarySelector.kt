package com.cornellappdev.transit.ui.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.Style

/**
 * Row of three mutually exclusive buttons
 */
@Composable
fun TernarySelector(
    firstButtonLabel: String,
    secondButtonLabel: String,
    thirdButtonLabel: String,
    buttonWidth: Dp,
    buttonHeight: Dp,
    selected: Int,
    onSelectChanged: (Int) -> Unit
) {

    val transition = updateTransition(targetState = selected, label = "Button Transition")

    // Animate the selector's horizontal position based on the selected button
    val selectorOffset by transition.animateDp(label = "Selector Offset") { targetSelected ->
        when (targetSelected) {
            0 -> 0.dp
            1 -> buttonWidth
            else -> 2 * buttonWidth
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Button backgrounds
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(3 * buttonWidth, buttonHeight)
                    .background(color = DividerGray, shape = RoundedCornerShape(8.dp))
            )
        }
        // Row for selector overlay, mirroring the layout of the buttons
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .offset(x = selectorOffset)
                    .size(
                        buttonWidth - 2.dp,
                        buttonHeight - 2.dp
                    ) // Adjust size based on button size
                    .padding(1.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            )
            Spacer(
                modifier = Modifier.size(2 * buttonWidth, buttonHeight)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // List of button titles
            val buttonTitles = listOf(firstButtonLabel, secondButtonLabel, thirdButtonLabel)

            // Iterate over button titles and create a button for each
            buttonTitles.forEachIndexed { index, title ->
                TextButton(
                    onClick = { onSelectChanged(index) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(size = 8.dp),
                    modifier = Modifier
                        .width(buttonWidth)
                        .height(buttonHeight)
                ) {
                    Text(
                        text = title,
                        style = Style.heading3,
                        fontWeight = if (selected == index) FontWeight.SemiBold else FontWeight.Normal
                    )
                }

            }
        }
    }
}