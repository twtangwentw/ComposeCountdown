/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.NotStarted
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.components.TimeSelectEnum
import com.example.androiddevchallenge.ui.components.TimeSelector
import com.example.androiddevchallenge.ui.components.Timing
import com.example.androiddevchallenge.ui.components.getTimeNumber
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            TopAppBar(title = { Text(text = stringResource(R.string.countdown)) })
            var timing by remember { mutableStateOf(false) }
            var pause by remember { mutableStateOf(false) }
            var hour by remember { mutableStateOf(0) }
            var minute by remember { mutableStateOf(0) }
            var second by remember { mutableStateOf(0) }
            val color = Color(0xFFE91E63)
            val style = TextStyle(
                fontFamily = FontFamily.Serif,
                lineHeight = 50.sp,
                letterSpacing = 4.sp,
                fontSize = 40.sp,
                fontWeight = FontWeight.W500
            )
            val coroutineScope = rememberCoroutineScope()
            val timeJob = remember {
                coroutineScope.launch(start = CoroutineStart.LAZY) {
                    while (true) {
                        delay(1000)
                        if (second == 0 && minute == 0 && hour == 0) {
                            pause = true
                        }
                        if (!timing || pause) {
                            continue
                        }
                        second = getTimeNumber(second - 1, TimeSelectEnum.SECOND)
                        if (second == 59) {
                            minute = getTimeNumber(minute - 1, TimeSelectEnum.MINUTE)
                            if (minute == 59)
                                hour = getTimeNumber(hour - 1, TimeSelectEnum.HOUR)
                        }
                    }
                }
            }
            if (timing && pause) {
                AlertDialog(
                    onDismissRequest = { },
                    confirmButton = {
                        Button(onClick = {
                            timing = false
                            pause = false
                        }) {
                            Text(text = "Yes")
                        }
                    },
                    text = {
                        Text("TimeEnds")
                    }
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Crossfade(targetState = timing) {
                    if (it) {
                        Timing(
                            hour = hour,
                            minute = minute,
                            second = second,
                            style = style,
                            color = color
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TimeSelector(
                                type = TimeSelectEnum.HOUR,
                                current = hour,
                                change = { hour = this },
                                style = style
                            )
                            Text(text = ":", color = color, style = style)
                            TimeSelector(
                                type = TimeSelectEnum.MINUTE,
                                current = minute,
                                change = { minute = this },
                                style = style
                            )
                            Text(text = ":", color = color, style = style)
                            TimeSelector(
                                type = TimeSelectEnum.SECOND,
                                current = second,
                                change = { second = this },
                                style = style
                            )
                        }
                    }
                }
            }
            Crossfade(targetState = timing) {
                if (it) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(156.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                timing = false
                                pause = false
                            },
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Stop,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                        Spacer(modifier = Modifier.requiredWidth(56.dp))
                        Button(
                            onClick = {
                                timing = true
                                pause = !pause
                            },
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Pause,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(156.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                timing = true
                                pause = false
                                if (timeJob.isActive.not()) {
                                    timeJob.start()
                                }
                            },
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowRight,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }

        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
