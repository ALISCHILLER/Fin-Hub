package com.msa.finhub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

// اگر فونت برند داری اینجا معرفی کن و در copy اعمال کن.
val AppTypography: Typography = Typography().run {
    copy(
        displayLarge   = displayLarge.copy   (fontWeight = FontWeight.SemiBold),
        headlineLarge  = headlineLarge.copy  (fontWeight = FontWeight.Bold),
        titleLarge     = titleLarge.copy     (fontWeight = FontWeight.SemiBold),
        titleMedium    = titleMedium.copy    (fontWeight = FontWeight.SemiBold),
        bodyLarge      = bodyLarge.copy      (),
        bodyMedium     = bodyMedium.copy     (),
        labelLarge     = labelLarge.copy     (fontWeight = FontWeight.Medium)
    )
}
