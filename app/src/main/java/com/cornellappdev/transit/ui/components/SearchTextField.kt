package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.DividerGray

/**
 * Creates a searching text field.
 *
 * @param value The text to be displayed
 * @param setValue The callback that is triggered when the input service updates the text.
 *      An updated text comes as a parameter of the callback.
 * @param placeholderText The placeholder to be displayed when the text field is in focus and the input text is empty
 * @param modifier Modifier to be applied to the [SearchTextField]
 * @param singleLine When set to true, this text field becomes
 *      a single horizontally scrolling text field instead of wrapping onto multiple lines.
 * @param prefix The composable to be rendered before the text
 * @param suffix The composable to be rendered after the text
 * @param height The height of the text field
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    value: String,
    setValue: (String) -> Unit,
    placeholderText: String,
    prefix: @Composable() (() -> Unit)? = null,
    suffix: @Composable() (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    phoneNumber: Boolean = false,
    singleLine: Boolean = false,
    height: Dp,
    textStyle: TextStyle = TextStyle(
        color = Color.Black,
        fontSize = 14.sp,
        textAlign = TextAlign.Start
    ),
    visualTransformation: VisualTransformation = None,
    wrapText: Boolean = false,
    maxLines: Int = 1
) {
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DividerGray,
            unfocusedContainerColor = DividerGray,
            cursorColor = Black,
            unfocusedBorderColor = DividerGray,
            focusedBorderColor = DividerGray,
        ),
        value = value,
        onValueChange = setValue,
        placeholder = {
            Text(
                text = placeholderText,
                color = Gray,
                fontSize = textStyle.fontSize
            )
        },
        modifier = if (wrapText)
            modifier
                .heightIn(min = height)
                .fillMaxWidth() else
            modifier
                .height(height)
                .fillMaxWidth(),
        textStyle = textStyle,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = if (phoneNumber) KeyboardType.Phone else KeyboardType.Text),
        visualTransformation = visualTransformation,
        maxLines = maxLines,
        prefix = prefix,
        suffix = suffix,
        shape = RoundedCornerShape(8.dp),

        )
}