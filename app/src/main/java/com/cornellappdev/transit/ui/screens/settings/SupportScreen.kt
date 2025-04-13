package com.cornellappdev.transit.ui.screens.settings

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.components.FAQ
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun SupportScreen(context: Context) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Text(
            text = "Support",
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            color = TransitBlue,
        )
        Text(
            text = "Report issues and contact Cornell AppDev",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp),
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            color = Color.Gray
        )

        Text(
            text = "Make Transit Better",
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
        )

        Text(
            text = "Help us improve Transit by letting us know what's wrong",
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp),
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            color = Color.LightGray
        )

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("team@cornellappdev.com"))
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = TransitBlue,
                contentColor = Color.White
            ),
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.report),
                    contentDescription = "Report an Issue",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(16.dp)
                )
                Text(
                    text = "Report an Issue",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp),
                    fontFamily = robotoFamily,
                    fontStyle = FontStyle.Normal,
                )
            }

        }

        Text(
            text = "Frequently Asked Questions",
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
        )

        FAQ(
            title = "How do I use this?",
            dropdownText = "Click here to learn more about how to use this feature."
        )

    }
}


@Preview(showBackground = true)
@Composable
fun SupportScreenPreview() {
    SupportScreen(LocalContext.current)
}