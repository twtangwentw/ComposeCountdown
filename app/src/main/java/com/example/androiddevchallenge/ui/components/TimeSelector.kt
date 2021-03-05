package com.example.androiddevchallenge.ui.components

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun TimeSelector(
    type: TimeSelectEnum,
    current: Int,
    change: Int.() -> Unit,
    color1: Color = Color(0xFFE91E63),
    color2: Color = Color(0xFFFCE4EC),
    style: TextStyle
) {
    val widthDp = with(LocalDensity.current) { style.fontSize.toDp() * 2 }
    val lineDp = with(LocalDensity.current) { style.lineHeight.toDp() }
    val linePx = with(LocalDensity.current) { style.lineHeight.toPx() }
    var offset by remember { mutableStateOf(0f) }
    val offsetDp = with(LocalDensity.current) { offset.toDp() }
    val coroutineScope = rememberCoroutineScope()
    val scrollableState = rememberScrollableState(consumeScrollDelta = {
        offset += it % linePx
        if (offset > linePx * 0.75) {
            offset -= linePx
            getTimeNumber(current - 1, type).change()
        } else if (offset < -linePx * 0.75) {
            offset += linePx
            getTimeNumber(current + 1, type).change()
        }
        it
    })
    if (scrollableState.isScrollInProgress.not() && offset != 0f) {
        coroutineScope.launch {
            val add =
                if (offset.absoluteValue > linePx * 0.5)
                    if (offset < 0) -linePx else linePx
                else 0f
            scrollableState.animateScrollBy(
                -offset + add
            )
        }
    }
    Column(
        modifier = Modifier
            .requiredHeight(lineDp * 5)
            .width(widthDp)
            .clipToBounds()
            .offset(y = offsetDp)
            .scrollable(scrollableState, Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val timeTextModifier = Modifier.height(lineDp)
        CompositionLocalProvider(LocalTextStyle provides style) {
            TimeText(
                text = current - 2,
                modifier = timeTextModifier,
                type = type,
                color = lerp(color1, color2, 0.8f - offset / linePx)
            )
            TimeText(
                text = current - 1,
                modifier = timeTextModifier,
                type = type,
                color = lerp(color1, color2, 0.4f - offset / linePx)
            )
            TimeText(
                text = current,
                modifier = timeTextModifier,
                type = type,
                color = lerp(color1, color2, 0f + offset.absoluteValue / linePx )
            )
            TimeText(
                text = current + 1,
                modifier = timeTextModifier,
                type = type,
                color = lerp(color1, color2, 0.4f + offset / linePx)
            )
            TimeText(
                text = current + 2,
                modifier = timeTextModifier,
                type = type,
                color = lerp(color1, color2, 0.8f + offset / linePx)
            )
        }
    }
}

fun getTimeNumber(current: Int, type: TimeSelectEnum): Int {
    val max = type.getMaxValue()
    return (current + max) % max
}

@Composable
fun TimeText(
    text: Int,
    modifier: Modifier,
    size: TextUnit = TextUnit.Unspecified,
    color: Color,
    type: TimeSelectEnum
) {
    val fontSize = if (size == TextUnit.Unspecified) LocalTextStyle.current.fontSize else size
    val timeNumber = getTimeNumber(text, type)
    val zero = if (timeNumber < 10) "0" else ""
    Log.i("TAG", "TimeText: $color")
    Text(text = "$zero$timeNumber", modifier = modifier, fontSize = fontSize, color = color)
}

/**
 * TypeOfTimeSelectSelector
 */
enum class TimeSelectEnum {
    HOUR,
    MINUTE,
    SECOND;
}

fun TimeSelectEnum.getMaxValue() = when (this) {
    TimeSelectEnum.HOUR -> 24
    TimeSelectEnum.MINUTE -> 60
    TimeSelectEnum.SECOND -> 60
}