package com.msa.finhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Error dialog با طراحی دلنشین: هدر گرادینت، آیکن دایره‌ای، دکمه‌های هماهنگ با تم.
 *
 * پارامترهای جدید (اختیاری):
 * - dismissText: متن دکمه‌ی ثانویه (اگر null باشد، دکمه نشان داده نمی‌شود)
 * - onRetry: اگر بدهی، دکمه‌ی «تلاش مجدد» اضافه می‌شود
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

                // هدر گرادینت
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.error,
                                    MaterialTheme.colorScheme.errorContainer
                                )
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // «آیکن» ساده و تمیز (بدون وابستگی آیکن)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.onError.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                }

                // متن خطا
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                )

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
                            )
                        ) { Text("تلاش مجدد") }
                        Spacer(Modifier.width(6.dp))
                    }
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) { Text(confirmText) }
                }
            }
        }
    }
}
