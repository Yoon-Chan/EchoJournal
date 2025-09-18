package com.chan.echojournal.echos.presentation.echos.models

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import com.chan.echojournal.core.presentation.designystem.theme.buttonGradient
import com.chan.echojournal.core.presentation.designystem.theme.buttonGradientIsPressed
import com.chan.echojournal.core.presentation.designystem.theme.primary90
import com.chan.echojournal.core.presentation.designystem.theme.primary95

data class BubbleFloatingActionButtonColors(
    val primary: Brush,
    val primaryPressed: Brush,
    val outerCircle: Brush,
    val innerCircle: Brush
)

@Composable
fun rememberBubbleFloatingActionButtonColors(
    primary: Brush = MaterialTheme.colorScheme.buttonGradient,
    primaryPressed: Brush = MaterialTheme.colorScheme.buttonGradientIsPressed,
    outerCircle: Brush = SolidColor(MaterialTheme.colorScheme.primary95),
    innerCircle: Brush = SolidColor(MaterialTheme.colorScheme.primary90),
): BubbleFloatingActionButtonColors {
    return remember(primary, primaryPressed, outerCircle, innerCircle) {
        BubbleFloatingActionButtonColors(
            primary,
            primaryPressed,
            outerCircle,
            innerCircle
        )
    }
}