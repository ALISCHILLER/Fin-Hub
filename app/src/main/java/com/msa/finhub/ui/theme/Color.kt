package com.msa.finhub.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// -------- Pastel Brand Tokens --------
// Periwinkle (Primary), Mint (Secondary), Rose (Tertiary)
val PastelPeriwinkle50  = Color(0xFFDDE2FF)
val PastelPeriwinkle200 = Color(0xFFBFC7FF)
val PastelPeriwinkle400 = Color(0xFF6E7FF3)
val PastelPeriwinkle700 = Color(0xFF39428A)

val PastelMint50   = Color(0xFFD8F6F0)
val PastelMint200  = Color(0xFFB8E8DC)
val PastelMint400  = Color(0xFF76D6C3)
val PastelMint700  = Color(0xFF2F6C61)

val PastelRose50   = Color(0xFFFFE1F1)
val PastelRose300  = Color(0xFFF7B2D9)
val PastelRose700  = Color(0xFF7D4967)

// Neutrals
val NeutralBackgroundLight = Color(0xFFF8FAFF)
val NeutralOnLight         = Color(0xFF1B212C)
val SurfaceLight           = Color(0xFFFFFFFF)
val SurfaceVariantLight    = Color(0xFFE6EAF6)
val OnSurfaceVariantLight  = Color(0xFF454C5A)
val OutlineLight           = Color(0xFF717889)

val NeutralBackgroundDark = Color(0xFF0F141D)
val NeutralOnDark         = Color(0xFFE6EAF6)
val SurfaceDark           = Color(0xFF141A24)
val SurfaceVariantDark    = Color(0xFF2B3240)
val OnSurfaceVariantDark  = Color(0xFFB5BCCB)
val OutlineDark           = Color(0xFF8A91A1)

// Errors (M3-like)
val ErrorLight      = Color(0xFFB3261E)
val OnErrorLight    = Color(0xFFFFFFFF)
val ErrorContainerL = Color(0xFFF9DEDC)
val OnErrorContL    = Color(0xFF410E0B)

val ErrorDark       = Color(0xFFF2B8B5)
val OnErrorDark     = Color(0xFF601410)
val ErrorContainerD = Color(0xFF8C1D18)
val OnErrorContD    = Color(0xFFF2B8B5)
// پالت رنگی برای متن و فیلدها
val brandText     = Color(0xFF1B212C)  // رنگ متن برند (تیره‌تر برای تضاد بیشتر با پس‌زمینه سفید)
val subText       = Color(0xFF5A6B83)  // رنگ متن فرعی (رنگ ملایم)
val cardBg        = Color(0xFFFFFFFF).copy(alpha = 0.96f) // پس‌زمینه کارت‌ها (سفید)
val fieldText     = Color(0xFF1B212C)  // متن فیلد (تیره‌تر)
val fieldLabel    = Color(0xFF39428A)  // رنگ برچسب فیلدها
val fieldHint     = Color(0xFF8DA0D6)  // رنگ راهنما در فیلدها
val fieldOutlineF = Color(0xFF7C9BFF)  // رنگ حاشیه در فیلدها (مناسب با تم روشن)
val fieldOutlineU = Color(0xFF3F4B72)  // رنگ حاشیه در فیلدها در حالت عدم تمرکز
val iconPrimary   = Color(0xFF7C9BFF)  // رنگ آیکن‌ها (با رنگ اصلی برند هماهنگ)
val muted         = Color(0xFFA7B4DA)  // رنگ کم‌رنگ برای متن‌های غیر ضروری
val buttonText    = Color(0xFF1B212C)  // رنگ متن دکمه‌ها (تیره‌تر برای خوانایی بیشتر)

// -------- Light Scheme (Pastel) --------
val FinHubLightColors = lightColorScheme(
    primary              = PastelPeriwinkle400,
    onPrimary            = Color.White,
    primaryContainer     = PastelPeriwinkle50,
    onPrimaryContainer   = Color(0xFF20253A),

    secondary            = PastelMint400,
    onSecondary          = Color(0xFF07332A),
    secondaryContainer   = PastelMint50,
    onSecondaryContainer = Color(0xFF0F2B26),

    tertiary             = PastelRose300,
    onTertiary           = Color(0xFF3C1227),
    tertiaryContainer    = PastelRose50,
    onTertiaryContainer  = Color(0xFF391227),

    background           = NeutralBackgroundLight,
    onBackground         = NeutralOnLight,
    surface              = SurfaceLight,
    onSurface            = NeutralOnLight,
    surfaceVariant       = SurfaceVariantLight,
    onSurfaceVariant     = OnSurfaceVariantLight,
    outline              = OutlineLight,

    error                = ErrorLight,
    onError              = OnErrorLight,
    errorContainer       = ErrorContainerL,
    onErrorContainer     = OnErrorContL
)

// -------- Dark Scheme (Pastel) --------
val FinHubDarkColors = darkColorScheme(
    primary              = Color(0xFFB9C3FF),         // Periwinkle brighten
    onPrimary            = Color(0xFF1C2240),
    primaryContainer     = PastelPeriwinkle700,
    onPrimaryContainer   = PastelPeriwinkle200,

    secondary            = Color(0xFFAEE9DD),         // Mint brighten
    onSecondary          = Color(0xFF0E2A24),
    secondaryContainer   = PastelMint700,
    onSecondaryContainer = PastelMint200,

    tertiary             = Color(0xFFFFC4E6),         // Rose brighten
    onTertiary           = Color(0xFF391227),
    tertiaryContainer    = PastelRose700,
    onTertiaryContainer  = PastelRose50,

    background           = NeutralBackgroundDark,
    onBackground         = NeutralOnDark,
    surface              = SurfaceDark,
    onSurface            = NeutralOnDark,
    surfaceVariant       = SurfaceVariantDark,
    onSurfaceVariant     = OnSurfaceVariantDark,
    outline              = OutlineDark,

    error                = ErrorDark,
    onError              = OnErrorDark,
    errorContainer       = ErrorContainerD,
    onErrorContainer     = OnErrorContD
)
