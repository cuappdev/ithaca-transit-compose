package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.HotspotBorderGray
import com.cornellappdev.transit.ui.theme.FavoritesYellow
import com.cornellappdev.transit.ui.theme.HotspotInputGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.robotoFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestHotspotSheet(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var netId by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var eventTypeExpanded by remember { mutableStateOf(false) }

    val eventTypeOptions = listOf(
        "Club Event",
        "Performance",
        "Study Session",
        "Other",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Request a HotSpot",
                fontFamily = robotoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = PrimaryText
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .background(color = HotspotInputGray, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close request hotspot sheet",
                    tint = PrimaryText
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledInputField(
                label = "Name",
                value = name,
                onValueChange = { name = it },
                placeholder = "Enter your Name",
            )

            LabeledInputField(
                label = "NetID",
                value = netId,
                onValueChange = { netId = it },
                placeholder = "Enter your NetID",
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Type of event",
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = PrimaryText
                )
                ExposedDropdownMenuBox(
                    expanded = eventTypeExpanded,
                    onExpandedChange = { eventTypeExpanded = !eventTypeExpanded },
                ) {
                    OutlinedTextField(
                        value = eventType,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Choose an option...",
                                color = MetadataGray,
                                fontFamily = robotoFamily,
                                fontSize = 16.sp,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "Choose event type",
                                tint = MetadataGray
                            )
                        },
                        colors = hotspotTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .height(52.dp)
                    )
                    DropdownMenu(
                        expanded = eventTypeExpanded,
                        onDismissRequest = { eventTypeExpanded = false },
                    ) {
                        eventTypeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    eventType = option
                                    eventTypeExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }

            LabeledInputField(
                label = "Description",
                value = description,
                onValueChange = { description = it },
                placeholder = "Add a description...",
                minLines = 5,
                maxLines = 5
            )

            LabeledInputField(
                label = "Location",
                value = location,
                onValueChange = { location = it },
                placeholder = "e.g. Duffield Hall",
            )

            PillButton(
                onClick = {},
                text = "Add Photos",
                colors = ButtonDefaults.buttonColors(
                    containerColor = DividerGray,
                    contentColor = SecondaryText
                ),
                textColor = SecondaryText,
                iconTint = SecondaryText
            )
        }

        PillButton(
            onClick = onSubmit,
            text = "Submit",
            iconResId = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = FavoritesYellow,
                contentColor = SecondaryText
            ),
            textColor = SecondaryText
        )
    }
}

@Composable
private fun LabeledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    maxLines: Int = 1,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = PrimaryText
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MetadataGray,
                    fontFamily = robotoFamily,
                    fontSize = 16.sp,
                )
            },
            shape = RoundedCornerShape(10.dp),
            minLines = minLines,
            maxLines = maxLines,
            colors = hotspotTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun hotspotTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = PrimaryText,
    unfocusedTextColor = PrimaryText,
    focusedContainerColor = HotspotInputGray,
    unfocusedContainerColor = HotspotInputGray,
    focusedBorderColor = HotspotBorderGray,
    unfocusedBorderColor = HotspotBorderGray,
    focusedPlaceholderColor = MetadataGray,
    unfocusedPlaceholderColor = MetadataGray,
    cursorColor = PrimaryText,
)

@Preview(showBackground = true)
@Composable
private fun RequestHotspotSheetPreview() {
    Box(
        modifier = Modifier.background(Color.White)
    ) {
        RequestHotspotSheet(
            onDismiss = {},
            onSubmit = {}
        )
    }
}
