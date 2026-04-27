package com.cornellappdev.transit.ui.screens.settings

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.components.MemberList
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.util.ABOUT_POD_LEADS
import com.cornellappdev.transit.util.ABOUT_TEAM_MEMBERS_BY_TEAM_SHUFFLED

/**
 * Composable for the About Screen of the app, which displays information about team behind it.
 */
@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    )

    {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = "Go back",
                tint = IconGray,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = stringResource(R.string.about_text),
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            color = TransitBlue,
        )
        Text(
            text = "Learn more about Cornell AppDev",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp),
            fontFamily = robotoFamily,
            color = Color.Gray
        )

        Image(
            painter = painterResource(id = R.drawable.appdev_gray),
            contentDescription = "Cornell AppDev Logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp)
                .size(30.dp)
        )

        Text(
            "DESIGNED AND DEVELOPED BY",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp),
            fontSize = 12.sp,
            fontFamily = robotoFamily,
            color = Color.Gray,
        )


        Text(
            buildAnnotatedString {
                append("Cornell")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("AppDev")
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp, bottom = 6.dp),
            fontSize = 36.sp,
            fontFamily = robotoFamily,
        )

        Row {
            Text(
                text = "Pod Leads",
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)
                    .width(80.dp)
            )
            MemberList(
                ABOUT_POD_LEADS
            )
        }

        for ((team, members) in ABOUT_TEAM_MEMBERS_BY_TEAM_SHUFFLED) {
            Row {
                Text(
                    text = team,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .align(Alignment.CenterVertically)
                        .width(80.dp)
                )
                MemberList(members)
            }
        }

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "https://www.cornellappdev.com/".toUri())
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.globe),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                tint = Color.Black
            )
            Text(text = "Visit Our Website", color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutScreenPreview() {
    AboutScreen {}
}