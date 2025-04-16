package com.cornellappdev.transit.ui.screens.settings

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
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
import kotlin.random.Random

@Composable
fun AboutScreen(context: Context) {
    var names = mapOf(
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
    )

    val shuffledNames = remember {
        names.entries.shuffled().associate { it.toPair() }
    }

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
            fontStyle = FontStyle.Normal,
            color = TransitBlue,
        )
        Text(
            text = "Learn more about Cornell AppDev",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp),
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
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
            fontStyle = FontStyle.Normal,
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

        for ((team, members) in shuffledNames) {
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

        Card(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(32.dp)
                )
                .clickable {
                    val intent =
                        Intent(Intent.ACTION_VIEW, "https://www.cornellappdev.com/".toUri())
                    context.startActivity(intent)
                }
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.globe),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 12.dp)
                )
                Text(
                    text = "Visit Our Website",
                    modifier = Modifier
                        .padding(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAboutScreen() {
    AboutScreen(LocalContext.current)
}