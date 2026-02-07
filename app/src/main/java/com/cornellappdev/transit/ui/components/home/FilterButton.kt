package com.cornellappdev.transit.ui.components.home

import android.R.attr.text
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R

@Composable
fun FilterButton(){
    Button(onClick = {}){
        Icon(
            painter = painterResource(R.drawable.filter_icon),
            contentDescription = "Filter Icon",
            modifier = Modifier.size(20.dp)
        )
        Text(text = "Filter", fontSize = 16.sp)
    }
}

@Preview
@Composable
fun FilterButtonPreview(){
    FilterButton()
}