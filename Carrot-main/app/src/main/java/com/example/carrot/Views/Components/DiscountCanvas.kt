package com.example.carrot.Views.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DiscountCanvas(text: String) {
    Box(
    ) {

        // Star drawing
        Canvas(
            modifier = Modifier
                .size(70.dp)
        ) {
            drawStar(
                color = Color.Red,
                numPoints = 10, // Number of points in the star
                radiusOuter = size.minDimension / 2,
                radiusInner = size.minDimension / 4
            )
        }

        // Discount percentage text inside the star
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

// Function to draw a star on Canvas
fun DrawScope.drawStar(
    color: Color,
    numPoints: Int,
    radiusOuter: Float,
    radiusInner: Float
) {
    val anglePerPoint = (360f / numPoints)
    val path = Path()

    for (i in 0 until numPoints * 2) {
        val angle = i * anglePerPoint / 2
        val radius = if (i % 2 == 0) radiusOuter else radiusInner
        val x = size.width / 2 + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
        val y = size.height / 2 + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    path.close()
    drawPath(path, color)
}