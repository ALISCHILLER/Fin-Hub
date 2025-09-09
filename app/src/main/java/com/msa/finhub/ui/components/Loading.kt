package com.msa.finhub.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * دیالوگ لودینگ بلوک‌کننده با گرادینت هدر و انیمیشن ساده‌ی پالس.
 */
@Composable
fun LoadingDialog(
    show: Boolean,
    text: String? = null
) {
    if (!show) return

    // انیمیشن ساده‌ی پالس برای نقطه‌ها
    val t = rememberInfiniteTransition(label = "loading-pulse")
    val a1 by t.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = LinearEasing), RepeatMode.Reverse
        ),
        label = "a1"
    )
    val a2 by t.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = LinearEasing, delayMillis = 150), RepeatMode.Reverse
        ),
        label = "a2"
    )
    val a3 by t.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = LinearEasing, delayMillis = 300), RepeatMode.Reverse
        ),
        label = "a3"
    )

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(Modifier.widthIn(min = 260.dp)) {

                // هدر گرادینت ظریف
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "در حال بارگذاری",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = text ?: "لطفاً صبر کنید…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(10.dp))
                        // سه نقطه‌ی پالس‌دار
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Dot(alpha = a1)
                            Spacer(Modifier.width(6.dp))
                            Dot(alpha = a2)
                            Spacer(Modifier.width(6.dp))
                            Dot(alpha = a3)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Dot(alpha: Float) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = alpha)
            )
    )
}

/**
 * اوورلی نیمه‌شفاف شیک (به‌جای دیالوگ پاپ‌آپ).
 */
@Composable
fun LoadingOverlay(
    show: Boolean,
    text: String? = null,
    modifier: Modifier = Modifier
) {
    if (!show) return
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
    ) {
        Surface(
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.width(16.dp))
                Text(
                    text = text ?: "در حال بارگذاری…",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
