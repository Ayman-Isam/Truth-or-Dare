package com.example.truthdare

import android.content.Context
import android.os.Build
import androidx.compose.material3.ButtonDefaults
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import com.example.truthdare.ui.theme.TruthDareTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TruthDareTheme {
                TruthDareApp()
            }
        }
    }
}

val darkGreen = Color(0,80,0)
val Red = Color(120,0,0)
val Purple = Color(76, 0, 153)
val Cyan = Color(0, 205, 224, 255)


@Preview
@Composable
fun TruthDareApp() {
    TruthDareWithButton(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.BottomCenter)
    )
}



@Composable
fun TruthDareWithButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val truths = context.resources.getStringArray(R.array.truths)
    val dares = context.resources.getStringArray(R.array.dares)
    var currentText by remember { mutableStateOf(truths[0]) }
    var isTruth by remember { mutableStateOf(true) }
    var consecutiveCount by remember { mutableStateOf(0) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Surface(
                color = Color.Black,
                modifier = Modifier.fillMaxSize(),

            ) {}
        }

        Text(
            text = if (isTruth) "Truth: $currentText" else "Dare: $currentText",
            fontSize = 30.sp,
            lineHeight = 40.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp,0.dp,20.dp,0.dp)
                .animateContentSize()
        )
        Button(
            onClick = {
                currentText = truths.random()
                isTruth = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(32, 255)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(32)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (20).dp)
                .offset(y = (-90).dp)
                .height(50.dp)
                .width(170.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = darkGreen ),
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(32, 255)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(200)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .align(Alignment.BottomCenter)
                .offset(x = (-20).dp)
                .offset(y = (-90).dp)
                .height(50.dp)
                .width(170.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Red ),
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(32, 255)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(400)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
                .height(50.dp)
                .width(352.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Purple )
            ) {
            Text(stringResource(R.string.random), fontSize = 25.sp)
        }
    }
}

