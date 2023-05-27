package com.example.truthdare

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.random.Random


@Preview
@Composable
fun HomeScreenPreview() {
    val truths = mutableListOf("Truth 1", "Truth 2")
    val dares = mutableListOf("Dare 1", "Dare 2")
    HomeScreen(
        navController = rememberNavController(),
        truths = truths,
        dares = dares
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
   navController:NavController,
   truths: MutableList<String>,
   dares: MutableList<String>
) {
    val context = LocalContext.current
    var currentText by remember { mutableStateOf("") }
    var isTruth by remember { mutableStateOf(true) }
    var consecutiveCount by remember { mutableStateOf(0) }

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                ), title = {
                    Text(
                        "Truth or Dare",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }, navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.refresh),
                            contentDescription = "Localized description",
                        )
                    }
                }, actions = {
                    IconButton(onClick = { navController.navigate(route = Screen.Add.route) {
                        popUpTo(Screen.Add.route) {
                            inclusive = true
                        }
                    }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    }
                })
            },
            content = { innerPadding ->
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if(currentText.isEmpty ()) "" else if (isTruth) "Truth: $currentText" else "Dare: $currentText",
                        fontSize = 30.sp,
                        lineHeight = 40.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = (-40).dp)
                            .padding(30.dp, 0.dp, 30.dp, 0.dp)
                            .animateContentSize(
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                    )
                    Button(
                        onClick = {
                            var newTruth: String
                            do {
                                newTruth = truths.random()
                            } while (newTruth == currentText)
                            currentText = newTruth
                            isTruth = true
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = (20).dp)
                            .offset(y = (-90).dp)
                            .height(50.dp)
                            .width(170.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary ),
                    )
                    {
                        Text(stringResource(R.string.truth), fontSize = 25.sp)
                    }

                    Button(
                        onClick = {
                            var newDare: String
                            do {
                                newDare = dares.random()
                            } while (newDare == currentText && !isTruth)
                            currentText = newDare
                            isTruth = false
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .align(Alignment.BottomCenter)
                            .offset(x = (-20).dp)
                            .offset(y = (-90).dp)
                            .height(50.dp)
                            .width(170.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary ),
                    ) {
                        Text(stringResource(R.string.dare), fontSize = 25.sp)
                    }

                    Button(
                        onClick = {
                            if (consecutiveCount >= 2) {
                                if (isTruth) {
                                    var newDare: String
                                    do {
                                        newDare = dares.random()
                                    } while (newDare == currentText && !isTruth)
                                    currentText = newDare
                                    isTruth = false
                                } else {
                                    currentText = truths.random()
                                    isTruth = true
                                }
                                consecutiveCount = 1
                            } else {
                                if (Random.nextBoolean()) {
                                    currentText = truths.random()
                                    if (isTruth) {
                                        consecutiveCount++
                                    } else {
                                        consecutiveCount = 1
                                    }
                                    isTruth = true
                                } else {
                                    var newDare: String
                                    do {
                                        newDare = dares.random()
                                    } while (newDare == currentText && !isTruth)
                                    currentText = newDare
                                    if (!isTruth) {
                                        consecutiveCount++
                                    } else {
                                        consecutiveCount = 1
                                    }
                                    isTruth = false
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .align(Alignment.BottomCenter)
                            .offset(y = (-20).dp)
                            .height(50.dp)
                            .width(352.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary )
                    ) {
                        Text(stringResource(R.string.random), fontSize = 25.sp)
                    }
                }
            },
        )
    }
}

