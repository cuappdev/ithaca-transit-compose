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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

private val names = mapOf(
    "iOS" to listOf(
        "Angelina Chen",
        "Asen Ou",
        "Jayson Hahn",
        "Daniel Chuang",
        "William Ma",
        "Sergio Diaz",
        "Kevin Chan",
        "Omar Rasheed",
        "Lucy Xu",
        "Haiying Weng",
        "Daniel Vebman",
        "Yana Sang",
        "Matt Barker",
        "Austin Astorga",
        "Monica Ong"
    ),
    "Android" to listOf(
        "Mihili Herath",
        "Jonathan Chen",
        "Veronica Starchenko",
        "Adam Kadhim",
        "Lesley Huang",
        "Kevin Sun",
        "Chris Desir",
        "Connor Reinhold",
        "Aastha Shah",
        "Justin Jiang",
        "Haichen Wang",
        "Jonvi Rollins",
        "Preston Rozwood",
        "Ziwei Gu",
        "Abdullah Islam"
    ),
    "Design" to listOf(
        "Gillian Fang",
        "Leah Kim",
        "Amy Ge",
        "Lauren Jun",
        "Zain Khoja",
        "Maggie Ying",
        "Femi Badero",
        "Maya Frai",
        "Mind Apivessa"
    ),
    "Marketing" to listOf(
        "Anvi Savant",
        "Christine Tao",
        "Luke Stewart",
        "Melika Khoshneviszadeh",
        "Eddie Chi",
        "Neha Malepati",
        "Emily Shiang",
        "Lucy Zhang",
        "Catherine Wei"
    ),
    "Backend" to listOf(
        "Nicole Qiu",
        "Daisy Chang",
        "Lauren Ah-Hot",
        "Maxwell Pang",
        "Mateo Weiner",
        "Cindy Liang",
        "Raahi Menon",
        "Kate Liang",
        "Alanna Zhou",
        "Kevin Chan",
        "Nate Schickler"
    )
).entries.shuffled().associate { it.toPair() }

/**
 * Composable for the About Screen of the app, which displays information about team behind it.
 */
@Composable
fun AboutScreen() {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    )

    {
        Text(
            text = "About Transit",
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
                listOf(
                    "Anvi Savant",
                    "Cindy Liang",
                    "Maxwell Pang",
                    "Amanda He",
                    "Connor Reinhold",
                    "Omar Rasheed",
                    "Maya Frai",
                    "Matt Barker"
                )
            )
        }

        for ((team, members) in names) {
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
private fun PreviewAboutScreen() {
    AboutScreen()
}