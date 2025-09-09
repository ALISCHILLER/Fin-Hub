package com.msa.finhub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush

data class AppGradients(
    val backgroundVertical: Brush,
    val primaryHorizontal: Brush
)

val LocalGradients = staticCompositionLocalOf<AppGradients> {
    // پیش‌فرض صرفاً برای جلوگیری از Null؛ همیشه از Theme تزریق می‌شود
    AppGradients(
        backgroundVertical = Brush.verticalGradient(emptyList()),
        primaryHorizontal  = Brush.horizontalGradient(emptyList())
    )
}

@Composable
fun rememberAppGradients(): AppGradients {
    val c = MaterialTheme.colorScheme
    return AppGradients(
        backgroundVertical = Brush.verticalGradient(
            listOf(c.primary.copy(alpha = 0.12f), c.surface)
        ),
        primaryHorizontal = Brush.horizontalGradient(
            listOf(c.primary, c.primaryContainer)
        )
    )
}
