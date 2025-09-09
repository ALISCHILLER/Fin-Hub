package com.msa.finhub.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

data class Spacing(
    val xs: Int = 4,
    val sm: Int = 8,
    val md: Int = 12,
    val lg: Int = 16,
    val xl: Int = 24,
    val xxl: Int = 32
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val Spacing.xsDp get() = xs.dp
val Spacing.smDp get() = sm.dp
val Spacing.mdDp get() = md.dp
val Spacing.lgDp get() = lg.dp
val Spacing.xlDp get() = xl.dp
val Spacing.xxlDp get() = xxl.dp
