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

/**
 * لوگوی حرفه‌ای: اُرب گرادیانی + رینگ براق چرخان + Z با گرادیان، کانتور، سایه‌ی نرم و کنتراست هوشمند.
 * - احترام به Reduced Motion (با پارامتر)
 * - اصلاح RoundRect با CornerRadius
 */
@Composable
fun FinHubLogoPro(
    modifier: Modifier = Modifier,
    dimension: Dp = 64.dp,
    baseColor: Color = MaterialTheme.colorScheme.primary,
    accentColor: Color = MaterialTheme.colorScheme.secondary,
    onBase: Color = MaterialTheme.colorScheme.onPrimary,
    animateRing: Boolean = true,
    preferReducedMotion: Boolean = false,
    contentDescription: String = "لوگوی حرفه‌ای فین‌هاب"
) {
    // انیمیشن چرخش رینگ (در صورت نیاز)
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

    // اگر onBase با baseColor کنتراست خوبی نداشت، بهترین رو انتخاب کن
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

        // ضخامت‌ها (مقیاس‌پذیر + حداقل منطقی)
        val minStroke = 2.dp.toPx()
        val ringWidth = max(r * 0.10f, minStroke)          // رینگ بیرونی
        val innerRingWidth = max(r * 0.03f, minStroke)     // رینگ داخلی ظریف
        val zThickness = max(r * 0.22f, 3.dp.toPx())       // ضخامت Z
        val zStroke = max(zThickness * 0.18f, 1.5.dp.toPx())
        val zShadowOffset = max(r * 0.03f, 1.5.dp.toPx())

        // 1) پس‌زمینه‌ی اُرب با گرادیان + ویگنت برای عمق
        val orbBrush = Brush.radialGradient(
            colors = listOf(
                baseColor.copy(alpha = 1f),
                baseColor.copy(alpha = 0.92f)
            ),
            center = center,
            radius = r
        )
        drawCircle(brush = orbBrush, radius = r, center = center)

        val vignette = Brush.radialGradient(
            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.18f)),
            center = center,
            radius = r * 1.05f
        )
        drawCircle(brush = vignette, radius = r, center = center, blendMode = BlendMode.Multiply)

        // 2) رینگ بیرونی براق با گرادیان چرخشی
        inset(ringWidth / 2f) {
            rotate(rotation) {
                val sweep = Brush.sweepGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.35f),
                        accentColor.copy(alpha = 0.22f),
                        Color.Transparent,
                        accentColor.copy(alpha = 0.40f),
                        Color.White.copy(alpha = 0.35f)
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

        // 3) رینگ داخلی ظریف برای جدا کردن Z از پس‌زمینه
        drawCircle(
            color = Color.White.copy(alpha = 0.08f),
            radius = r - ringWidth - innerRingWidth / 2f,
            style = Stroke(width = innerRingWidth)
        )

        // 4) ساخت Z (نوار بالا + مورب ضخیم + نوار پایین)
        inset(ringWidth + innerRingWidth + max(1f, zStroke)) {
            val w = size.width
            val h = size.height

            val left = w * 0.20f
            val right = w * 0.80f
            val top = h * 0.24f
            val bottom = h * 0.76f
            val width = right - left

            // نوارهای بالا/پایین با گوشه‌های نرم — اصلاح RoundRect با CornerRadius
            val rx = zThickness * 0.28f
            val ry = zThickness * 0.28f
            val corner = CornerRadius(rx, ry)

            val topBar = Path().apply {
                addRoundRect(
                    RoundRect(left, top, right, top + zThickness, corner)
                )
            }
            val bottomBar = Path().apply {
                addRoundRect(
                    RoundRect(left, bottom - zThickness, right, bottom, corner)
                )
            }

            // مورب ضخیم به صورت چهارضلعی موازی با ضخامت واقعی
            val p1 = Offset(right - zThickness * 0.15f, top + zThickness) // اتصال تمیزتر
            val p2 = Offset(left + zThickness * 0.15f, bottom - zThickness)
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

            // --- سایه‌ی نرم زیر Z (چند پاس با جابه‌جایی) ---
            val shadowColor = Color.Black.copy(alpha = 0.22f)
            val shadowPasses = 3
            repeat(shadowPasses) { i ->
                val k = (i + 1) / shadowPasses.toFloat()
                val off = zShadowOffset * k
                val alpha = 0.22f * (1f - k * 0.6f)
                val sc = shadowColor.copy(alpha = alpha)

                translate(off, off) {
                    drawPath(topBar, color = sc)
                    drawPath(diag, color = sc)
                    drawPath(bottomBar, color = sc)
                }
            }

            // پرکننده‌ی Z با گرادیان
            val zFill = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.95f),
                    autoOn.copy(alpha = 0.96f),
                    autoOn.copy(alpha = 0.82f),
                ),
                start = Offset(left, top),
                end = Offset(right, bottom)
            )

            // کانتور Z
            val zEdge = autoOn.copy(alpha = 0.28f)

            // رسم: ابتدا پر، سپس کانتور
            drawPath(topBar, brush = zFill)
            drawPath(diag, brush = zFill)
            drawPath(bottomBar, brush = zFill)

            drawPath(
                topBar,
                color = zEdge,
                style = Stroke(width = zStroke, join = StrokeJoin.Round, cap = StrokeCap.Round)
            )
            drawPath(
                diag,
                color = zEdge,
                style = Stroke(width = zStroke, join = StrokeJoin.Round, cap = StrokeCap.Round)
            )
            drawPath(
                bottomBar,
                color = zEdge,
                style = Stroke(width = zStroke, join = StrokeJoin.Round, cap = StrokeCap.Round)
            )

            // هایلایت شیشه‌ای روی Z (BlendMode.Screen)
            val zHighlight = Brush.linearGradient(
                colors = listOf(Color.White.copy(alpha = 0.40f), Color.Transparent),
                start = Offset(left, top),
                end = Offset(right, top + width * 0.35f)
            )
            drawPath(topBar, brush = zHighlight, blendMode = BlendMode.Screen)
        }

        // 5) گلاس‌لاین روی اُرب: قوس براق (Screen)
        inset(ringWidth) {
            val gloss = Brush.linearGradient(
                colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent),
                start = Offset(center.x - r, center.y - r * 0.6f),
                end = Offset(center.x + r, center.y - r * 0.1f)
            )
            drawArc(
                brush = gloss,
                startAngle = -200f,
                sweepAngle = 80f,
                useCenter = false,
                style = Stroke(width = r * 0.20f, cap = StrokeCap.Round),
                blendMode = BlendMode.Screen
            )
        }
    }
}

/* ==================== Utilities ==================== */

/** در صورت کمبود کنتراست، بهترین onColor (کاندید/سفید/مشکی) را برمی‌گرداند. */
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

/** ترنسلیت ساده داخل DrawScope (برای سایه‌ی چندپاسی). */
private inline fun androidx.compose.ui.graphics.drawscope.DrawScope.translate(
    dx: Float,
    dy: Float,
    block: androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit
) = withTransform({ translate(left = dx, top = dy) }) { block() }
