package com.sunny.handbook.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunny.handbook.ui.theme.HBBlue


@Preview
@Composable
fun LoadingIndicator() {


    val s1x = remember { Animatable(0f) }
    val s1y = remember { Animatable(0f) }
    val s2x = remember { Animatable(1f) }
    val s2y = remember { Animatable(0f) }
    val s3x = remember { Animatable(0f) }
    val s3y = remember { Animatable(1f) }
    LaunchedEffect(key1 = true, block = {
        while (true) {
            s2y.animateTo(1f, tween(200))
            s1x.animateTo(1f, tween(200))
            s3y.animateTo(0f, tween(200))
            s2x.animateTo(0f, tween(200))
            s1y.animateTo(1f, tween(200))
            s3x.animateTo(1f, tween(200))
            s2y.animateTo(0f, tween(200))
            s1x.animateTo(0f, tween(200))
            s3y.animateTo(1f, tween(200))
            s2x.animateTo(1f, tween(200))
            s1y.animateTo(0f, tween(200))
            s3x.animateTo(0f, tween(200))
        }

    })
    Canvas(modifier = Modifier.size(100.dp), onDraw = {

        val width = size.width

        drawPath(
            path = Path().apply {
                moveTo(s1x.value * width, s1y.value * width)
                lineTo(s2x.value * width, s2y.value * width)
                lineTo(s3x.value * width, s3y.value * width)
            },
            color = HBBlue,
            style = Fill
        )

    })
}
