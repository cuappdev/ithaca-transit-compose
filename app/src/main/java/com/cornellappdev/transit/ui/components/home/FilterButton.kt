package com.cornellappdev.transit.ui.components.home

import android.R.attr.text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FilterButton(){
    Button(onClick = {}){
        Icon(painter = androidx.compose.ui.res.painterResource(id = com.cornellappdev.transit.R.drawable.filter_icon), contentDescription = "Filter Icon")
        Text(text = "Filter")
    }
}

@Preview
@Composable
fun FilterButtonPreview(){
    FilterButton()
}