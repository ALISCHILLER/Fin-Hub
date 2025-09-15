package com.msa.finhub.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.sqrt

private val LogoBaseBlue = Color(0xFF0052D4)
private val LogoAccentCyan = Color(0xFF00C6FF)

/**
 * لوگوی حرفه‌ای: اُرب گرادیانی + رینگ براق چرخان + Z ارتقایافته (Ribbon + Fold + Highlight)
 * - سازگار با Reduced Motion (غیرفعال کردن انیمیشن با preferReducedMotion)
 * - کنتراست هوشمند برای onColor
 */
@Composable
fun FinHubLogoPro(
    modifier: Modifier = Modifier,
    dimension: Dp = 64.dp,
    baseColor: Color = LogoBaseBlue,
    accentColor: Color = LogoAccentCyan,
    onBase: Color = Color.White,
    animateRing: Boolean = true,
    preferReducedMotion: Boolean = false,
    contentDescription: String = "لوگوی حرفه‌ای فین‌هاب",
    zTiltDegrees: Float = -6f // کمی کج برای حس حرکت
) {
    val effectiveAnimate = animateRing && !preferReducedMotion
    val rotation: Float = if (effectiveAnimate) {
        val t = rememberInfiniteTransition(label = "ring")
        val r by t.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(animation = tween(4200, easing = LinearEasing)),
            label = "angle"
        )
        r
    } else 22f

    // اگر onBase خوانا نباشه، بهترین رو انتخاب کن
    val autoOn = remember(baseColor, onBase) { bestReadableOn(baseColor, onBase) }

    Canvas(
        modifier = modifier
            .size(dimension)
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Image
            }
    ) {
        val r = size.minDimension / 2f
        val center = this.center

        // ضخامت‌ها
        val minStroke = 2.dp.toPx()
        val ringWidth = max(r * 0.10f, minStroke)
        val innerRingWidth = max(r * 0.03f, minStroke)
        val zThickness = max(r * 0.22f, 3.dp.toPx())
        val zStroke = max(zThickness * 0.18f, 1.5.dp.toPx())
        val zShadowOffset = max(r * 0.03f, 1.5.dp.toPx())

        // 1) اُرب گرادیانی + ویگنت
        val orbBrush = Brush.radialGradient(
            colors = listOf(baseColor.lighten(0.10f), baseColor.darken(0.10f)),
            center = center, radius = r
        )
        drawCircle(brush = orbBrush, radius = r, center = center)

        val vignette = Brush.radialGradient(
            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.18f)),
            center = center, radius = r * 1.05f
        )
        drawCircle(brush = vignette, radius = r, center = center, blendMode = BlendMode.Multiply)

        // 2) رینگ براق
        inset(ringWidth / 2f) {
            rotate(rotation) {
                val sweep = Brush.sweepGradient(
                    colors = listOf(
                        accentColor.lighten(0.25f), accentColor,
                        accentColor.darken(0.20f), accentColor,
                        accentColor.lighten(0.25f)
                    ),
                    center = center
                )
                drawCircle(
                    brush = sweep,
                    radius = r - ringWidth / 2f,
                    style = Stroke(width = ringWidth, cap = StrokeCap.Round)
                )
            }
        }

        // 3) رینگ داخلیِ جداکننده
        drawCircle(
            color = Color.White.copy(alpha = 0.08f),
            radius = r - ringWidth - innerRingWidth / 2f,
            style = Stroke(width = innerRingWidth)
        )

        // 4) Z ارتقایافته
        inset(ringWidth + innerRingWidth + max(1f, zStroke)) {
            val w = size.width
            val h = size.height

            // باکس Z با کمی overshoot
            val left = w * 0.20f
            val right = w * 0.80f
            val top = h * 0.235f
            val bottom = h * 0.765f
            val width = right - left
            val pivot = Offset((left + right) / 2f, (top + bottom) / 2f)

            rotate(zTiltDegrees, pivot) {
                // نوارهای بالا/پایین با گوشه‌های نرم (CornerRadius + RoundRect)
                val corner = CornerRadius(zThickness * 0.26f, zThickness * 0.26f)
                val topBar = Path().apply {
                    addRoundRect(RoundRect(left, top, right, top + zThickness, corner))
                }
                val bottomBar = Path().apply {
                    addRoundRect(RoundRect(left, bottom - zThickness, right, bottom, corner))
                }

                // مورب نواری (چهارضلعی موازی) با اتصال تمیز
                val p1 = Offset(right - zThickness * 0.12f, top + zThickness)
                val p2 = Offset(left + zThickness * 0.12f, bottom - zThickness)
                val vx = p2.x - p1.x
                val vy = p2.y - p1.y
                val len = sqrt((vx * vx + vy * vy).toDouble()).toFloat().coerceAtLeast(1e-3f)
                val nx = -vy / len
                val ny = vx / len
                val ox = nx * (zThickness / 2f)
                val oy = ny * (zThickness / 2f)
                val diag = Path().apply {
                    moveTo(p1.x + ox, p1.y + oy)
                    lineTo(p2.x + ox, p2.y + oy)
                    lineTo(p2.x - ox, p2.y - oy)
                    lineTo(p1.x - ox, p1.y - oy)
                    close()
                }

                // سایه‌ی نرم زیر Z (سه پاس)
                val shadowColor = Color.Black.copy(alpha = 0.22f)
                repeat(3) { i ->
                    val k = (i + 1) / 3f
                    val off = zShadowOffset * k
                    val sc = shadowColor.copy(alpha = 0.22f * (1f - k * 0.6f))
                    translate(off, off) {
                        drawPath(topBar, color = sc)
                        drawPath(diag, color = sc)
                        drawPath(bottomBar, color = sc)
                    }
                }

                // پرکننده‌ی Z
                val zFill = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f),
                        autoOn.copy(alpha = 0.96f),
                        autoOn.copy(alpha = 0.84f)
                    ),
                    start = Offset(left, top),
                    end = Offset(right, bottom)
                )
                val zEdge = autoOn.copy(alpha = 0.28f)

                drawPath(topBar, brush = zFill)
                drawPath(diag, brush = zFill)
                drawPath(bottomBar, brush = zFill)

                // کانتور با پارامترهای نام‌دار (حل mismatch)
                drawPath(
                    topBar,
                    color = zEdge,
                    style = Stroke(width = zStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    diag,
                    color = zEdge,
                    style = Stroke(width = zStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    bottomBar,
                    color = zEdge,
                    style = Stroke(width = zStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // هایلایت مرکزی (شیشه‌ای) روی هر بخش
                val centerStroke = zThickness * 0.38f
                val centerAlpha = 0.35f

                val hlTop = Path().apply {
                    moveTo(left + zThickness * 0.55f, top + zThickness / 2f)
                    lineTo(right - zThickness * 0.55f, top + zThickness / 2f)
                }
                drawPath(
                    hlTop,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = centerAlpha), Color.Transparent),
                        start = Offset(left, top), end = Offset(right, top)
                    ),
                    style = Stroke(width = centerStroke, cap = StrokeCap.Round),
                    blendMode = BlendMode.Screen
                )

                val hlDiag = Path().apply { moveTo(p1.x, p1.y); lineTo(p2.x, p2.y) }
                drawPath(
                    hlDiag,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = centerAlpha), Color.Transparent),
                        start = p1, end = p2
                    ),
                    style = Stroke(width = centerStroke, cap = StrokeCap.Round),
                    blendMode = BlendMode.Screen
                )

                val hlBottom = Path().apply {
                    moveTo(left + zThickness * 0.55f, bottom - zThickness / 2f)
                    lineTo(right - zThickness * 0.55f, bottom - zThickness / 2f)
                }
                drawPath(
                    hlBottom,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = centerAlpha), Color.Transparent),
                        start = Offset(left, bottom), end = Offset(right, bottom)
                    ),
                    style = Stroke(width = centerStroke, cap = StrokeCap.Round),
                    blendMode = BlendMode.Screen
                )

                // افکت Fold ظریف در اتصال‌ها
                val foldSize = zThickness * 0.65f
                val topFold = Path().apply {
                    moveTo(p1.x, p1.y)
                    lineTo(p1.x - foldSize, p1.y)
                    lineTo(p1.x, p1.y + foldSize)
                    close()
                }
                drawPath(
                    topFold,
                    brush = Brush.linearGradient(
                        listOf(Color.Black.copy(alpha = 0.22f), Color.Transparent),
                        start = Offset(p1.x - foldSize, p1.y),
                        end = Offset(p1.x, p1.y + foldSize)
                    ),
                    blendMode = BlendMode.Multiply
                )

                val bottomFold = Path().apply {
                    moveTo(p2.x, p2.y)
                    lineTo(p2.x + foldSize, p2.y)
                    lineTo(p2.x, p2.y - foldSize)
                    close()
                }
                drawPath(
                    bottomFold,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.28f), Color.Transparent),
                        start = Offset(p2.x + foldSize, p2.y),
                        end = Offset(p2.x, p2.y - foldSize)
                    ),
                    blendMode = BlendMode.Screen
                )
            }
        }

        // 5) گلاس‌لاین روی اُرب
        inset(ringWidth) {
            val gloss = Brush.linearGradient(
                colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent),
                start = Offset(center.x - r, center.y - r * 0.6f),
                end = Offset(center.x + r, center.y - r * 0.1f)
            )
            drawArc(
                brush = gloss,
                startAngle = -200f, sweepAngle = 80f, useCenter = false,
                style = Stroke(width = r * 0.20f, cap = StrokeCap.Round),
                blendMode = BlendMode.Screen
            )
        }
    }
}

/* ==================== Utilities ==================== */

private fun bestReadableOn(base: Color, candidate: Color): Color {
    fun contrastRatio(fg: Color, bg: Color): Double {
        val l1 = fg.luminance().toDouble()
        val l2 = bg.luminance().toDouble()
        val (hi, lo) = if (l1 >= l2) l1 to l2 else l2 to l1
        return (hi + 0.05) / (lo + 0.05)
    }
    val options = listOf(candidate, Color.White, Color.Black)
    val best = options.maxBy { contrastRatio(it, base) }
    val ratio = contrastRatio(best, base)
    return if (ratio >= 4.5) best else {
        val wb = if (contrastRatio(Color.White, base) >= contrastRatio(Color.Black, base)) Color.White else Color.Black
        wb.copy(alpha = best.alpha)
    }
}

private fun Color.lighten(factor: Float) = copy(
    red = (red + (1 - red) * factor).coerceIn(0f, 1f),
    green = (green + (1 - green) * factor).coerceIn(0f, 1f),
    blue = (blue + (1 - blue) * factor).coerceIn(0f, 1f)
)

private fun Color.darken(factor: Float) = copy(
    red = (red * (1 - factor)).coerceIn(0f, 1f),
    green = (green * (1 - factor)).coerceIn(0f, 1f),
    blue = (blue * (1 - factor)).coerceIn(0f, 1f)
)

private inline fun androidx.compose.ui.graphics.drawscope.DrawScope.translate(
    dx: Float,
    dy: Float,
    block: androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit
) = withTransform({ translate(left = dx, top = dy) }) { block() }
