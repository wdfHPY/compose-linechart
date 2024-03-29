package com.jetpack.compose.linechart

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jetpack.compose.linechart.charts.ChartWithAxis
import com.jetpack.compose.linechart.charts.LineChart
import com.jetpack.compose.linechart.charts.MainViewModel
import com.qmuiteam.qmui.util.QMUIDeviceHelper
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import jetpack.compose.gesture.*
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isTablet = QMUIDeviceHelper.isTablet(applicationContext)
        if (isTablet) {
            QMUIDisplayHelper.setFullScreen(this)
        }

        setContent {
/*            WindowPop(
                "xxxxx",
                "yyyyyy"
            )*/
//            ChartWithAxis()
/*            Column(modifier = Modifier.fillMaxSize()) {
                val chartWidth = viewModel.chartWidth.collectAsState()
                val chartHeight = viewModel.chartHeight.collectAsState()
                LineChart(
                    chartHeight = chartHeight.value,
                    chartWidth = chartWidth.value,
                )

                var offsetX by remember { mutableStateOf(800f) }

                Text(
                    modifier = Modifier
                        .offset { IntOffset(x = offsetX.roundToInt(), 1000) }
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                offsetX += delta
                                viewModel.updateNewWidth(delta)
                            }
                        ),
                    text = "Drag me!"
                )
            }*/
            Column(modifier = Modifier.fillMaxSize()) {
/*                ScrollableBox()
                Scrollable()
                Scroll()*/
                DragModifier()
                DragScope2()
                TransformableState()
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Preview(widthDp = 1000, heightDp = 1000)
@Composable
fun DefaultPreview(
    str: String = "Hello Compose"
) {
    val xPosition = remember {
        mutableStateOf<Float>(0.0F)
    }

    val yPosition = remember {
        mutableStateOf<Float>(0.0F)
    }
    val path = remember {
        mutableStateOf(Path())
    }

    val displayState = remember {
        mutableStateOf(true)
    }

    val yAxisPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 20F
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textAlign = android.graphics.Paint.Align.LEFT
    }

    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = 1000,
                easing = FastOutLinearInEasing
            )
        )
    }

    val enterHorizontalExpand = remember {
        expandHorizontally(animationSpec = tween(100))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray), contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = displayState.value,
            enter = enterFadeIn + enterHorizontalExpand
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            displayState.value = true
                            xPosition.value = it.x - 50
                            yPosition.value = it.y - 154
                            path.value.reset()
                            path.value.moveTo(it.x, it.y)
                            path.value.lineTo(it.x - 10, it.y - 36)
                            path.value.moveTo(it.x, it.y)
                            path.value.lineTo(it.x + 20, it.y - 36)
                            path.value.lineTo(it.x - 20, it.y - 36)
                        }
/*                        MotionEvent.ACTION_MOVE -> {
                            displayState.value = false
                        }*/
                        else -> false
                    }
                    true
                }
            ) {
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(xPosition.value, yPosition.value),
                    cornerRadius = CornerRadius(x = 20F, y = 20F),
                    size = Size(500F, 120F)
                )
                drawPath(
                    path = path.value,
                    color = Color.White
                )

                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        "xAxis Display SomeThing",
                        xPosition.value + 10,
                        yPosition.value + 50,
                        yAxisPaint
                    )
                }

                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        "yAxis Display SomeThing",
                        xPosition.value + 10,
                        yPosition.value + 100,
                        yAxisPaint
                    )
                }
            }
        }

    }
}