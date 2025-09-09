package com.msa.finhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Error dialog سفارشی با هدر گرادینت و اکشن‌های انعطاف‌پذیر.
 *
 * بهبودها:
 * - پشتیبانی از پیام‌های بلند (اسکرول + محدودیت ارتفاع)
 * - دسترس‌پذیری بهتر (heading برای عنوان، آیکن تزئینی بدون خوانده‌شدن)
 * - پارامترهای قابل شخصی‌سازی برای گرادینت و آیکن
 * - رفتار استانداردتر رنگ‌ها (errorContainer / onErrorContainer)
 */
@Composable
fun ErrorDialog(
    message: String,
    title: String = "خطا",
    confirmText: String = "باشه",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss,
    // اختیاری‌ها:
    dismissText: String? = null,
    onRetry: (() -> Unit)? = null,
    headerBrush: Brush? = null,                    // اگر null باشد، گرادینت پیش‌فرض اعمال می‌شود
    icon: (@Composable () -> Unit)? = null,         // اگر null باشد، آیکن پیش‌فرض دایره‌ای نشان داده می‌شود
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )
) {
    Dialog(onDismissRequest = onDismiss, properties = properties) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // هدر گرادینت (قابل شخصی‌سازی)
                val brush = headerBrush ?: Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.error,
                        MaterialTheme.colorScheme.errorContainer
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // آیکن: پیش‌فرض دایره با "!"، قابل جایگزینی با پارامتر [icon]
                        if (icon != null) {
                            Box(Modifier.clearAndSetSemantics { }) { icon() }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.onError.copy(alpha = 0.15f)
                                    )
                                    .clearAndSetSemantics { }, // تزئینی؛ توسط TalkBack خوانده نشود
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "!",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onError
                                )
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.semantics { heading() } // عنوان دیالوگ
                        )
                    }
                }

                // متن خطا (با اسکرول و محدودیت ارتفاع)
                val scroll = rememberScrollState()
                if (message.isNotBlank()) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .verticalScroll(scroll)
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }

                // اکشن‌ها
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (dismissText != null) {
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) { Text(dismissText) }
                        Spacer(Modifier.width(6.dp))
                    }

                    if (onRetry != null) {
                        OutlinedButton(
                            onClick = onRetry,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = ButtonDefaults.outlinedButtonBorder // مرز پیش‌فرض
                        ) { Text("تلاش مجدد") }
                        Spacer(Modifier.width(6.dp))
                    }

                    // دکمهٔ اصلی با کنتراست استانداردتر
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) { Text(confirmText) }
                }
            }
        }
    }
}
