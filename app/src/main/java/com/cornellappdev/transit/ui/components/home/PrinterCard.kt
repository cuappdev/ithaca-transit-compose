package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.WarningOrange

/**
 * Card for a printer
 */
@Composable
fun PrinterCard(
    title: String,
    subtitle: String,
    inColor: Boolean,
    hasCopy: Boolean,
    hasScan: Boolean,
    alertMessage: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = Style.cardH1,
                        color = PrimaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                    Text(
                        text = subtitle,
                        style = Style.heading3,
                        color = SecondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                    if (alertMessage.isNotBlank()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.warning),
                                contentDescription = null,
                                tint = WarningOrange,
                            )
                            Text(
                                text = alertMessage,
                                style = Style.heading3,
                                color = WarningOrange,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(end = 32.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.printer_image),
                            contentDescription = null,
                        )
                        if (inColor) {
                            Tag(
                                name = "Color",
                                iconRes = R.drawable.color_wheel
                            )
                        } else {
                            Tag(
                                name = "BW",
                                iconRes = R.drawable.black_white_wheel
                            )
                        }

                        if (hasCopy) {
                            Tag(name = "Copy")
                        }

                        if (hasScan) {
                            Tag(name = "Scan")
                        }
                    }
                }

                FavoritesStar(onFavoriteClick = onFavoriteClick, isFavorite = isFavorite)
            }

        }
    }
}

@Composable
private fun Tag(
    name: String,
    iconRes: Int? = null,
) {
    Box(
        modifier = Modifier
            .border(
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(60.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (iconRes != null) {
                Image(painter = painterResource(id = iconRes), contentDescription = null)
            }

            Text(
                text = name,
                style = Style.heading3Semibold,
                color = PrimaryText,
            )
        }
    }
}

@Preview
@Composable
private fun PrinterCardPreview() {
    PrinterCard(
        title = "Akew:kon",
        subtitle = "Room 115",
        inColor = true,
        hasCopy = true,
        hasScan = true,
        alertMessage = "Residents Only",
        isFavorite = false,
        onFavoriteClick = {},
        onClick = {},
    )
}