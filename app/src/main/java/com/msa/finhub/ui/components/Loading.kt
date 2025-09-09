package com.msa.finhub.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.interaction.MutableInteractionSource

/**
 * دیالوگ لودینگ بلوک‌کننده با گرادینت هدر و انیمیشن نقطه‌های پالس.
 *
 * بهبودها:
 * - پارامتر progress (۰..۱) برای حالت determinate
 * - کنترل cancelable از بیرون
 * - Brush هدر و عنوان قابل‌سفارشی‌سازی
 * - A11y: liveRegion برای اعلام تغییر وضعیت
 */
@Composable
fun LoadingDialog(
    show: Boolean,
    text: String? = null,
    title: String = "در حال بارگذاری",
    progress: Float? = null,                 // اگر مقدار بدهید، Progress determinate می‌شود (۰..۱)
    cancelable: Boolean = false,
    headerBrush: Brush? = null,
    onDismissRequest: (() -> Unit)? = null
) {
    if (!show) return

    // انیمیشن ساده‌ی پالس برای سه نقطه
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
        onDismissRequest = {
            if (cancelable) onDismissRequest?.invoke()
        },
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.semantics {
                contentDescription = text ?: title
                liveRegion = LiveRegionMode.Polite
            }
        ) {
            Column(Modifier.widthIn(min = 260.dp, max = 420.dp)) {

                // هدر گرادینت (قابل‌سفارشی‌سازی)
                val brush = headerBrush ?: Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (progress == null) {
                        CircularProgressIndicator()
                    } else {
                        CircularProgressIndicator(progress = { progress.coerceIn(0f, 1f) })
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = text ?: "لطفاً صبر کنید…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(10.dp))
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
            .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
    )
}

/**
 * اوورلی نیمه‌شفاف که ورودی‌ها را واقعاً بلوکه می‌کند.
 *
 * - با blockTouches=true، رو لایه کلیک‌/لمس مصرف می‌شود و چیزی از زیر عبور نمی‌کند.
 */
@Composable
fun LoadingOverlay(
    show: Boolean,
    text: String? = null,
    blockTouches: Boolean = true,
    modifier: Modifier = Modifier
) {
    if (!show) return

    val scrim = MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scrim)
            .then(
                if (blockTouches)
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {} // مصرف ایونت‌ها
                    )
                else Modifier
            )
            .semantics {
                contentDescription = text ?: "در حال بارگذاری…"
                liveRegion = LiveRegionMode.Polite
            }
    ) {
        Surface(
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.width(16.dp))
                Text(
                    text = text ?: "در حال بارگذاری…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
