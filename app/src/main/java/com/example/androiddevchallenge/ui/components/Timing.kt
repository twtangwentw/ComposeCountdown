package com.example.androiddevchallenge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Timing(hour: Int, minute: Int, second: Int, style: TextStyle, color: Color) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        val modifier = Modifier
        CompositionLocalProvider(LocalTextStyle provides style) {
            TimeText(text = hour, modifier = modifier, color = color, type = TimeSelectEnum.HOUR)
            Text(text = ":", color = color)
            TimeText(text = minute, modifier = modifier, color = color, type = TimeSelectEnum.MINUTE)
            Text(text = ":", color = color)
            TimeText(text = second, modifier = modifier, color = color, type = TimeSelectEnum.SECOND)
        }
    }
}