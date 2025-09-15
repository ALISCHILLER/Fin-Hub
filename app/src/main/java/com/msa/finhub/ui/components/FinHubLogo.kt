package com.msa.finhub.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * لوگوی حرفه‌ای: اُرب گرادیانی + رینگ براق + Z ضخیم با گرادیان و کانتور
 */
@Composable
fun FinHubLogoPro(
    modifier: Modifier = Modifier,
    dimension: Dp = 64.dp,
    baseColor: Color = MaterialTheme.colorScheme.primary,
    accentColor: Color = MaterialTheme.colorScheme.secondary,
    onBase: Color = MaterialTheme.colorScheme.onPrimary,
    animateRing: Boolean = true,
    contentDescription: String = "لوگوی حرفه‌ای فین‌هاب"
) {
    // انیمیشن چرخش برای رینگ براق
    val rotation by androidx.compose.animation.core.rememberInfiniteTransition(label = "ring")
        .animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                animation = androidx.compose.animation.core.tween(4200, easing = androidx.compose.animation.core.LinearEasing)
            ),
            label = "angle"
        )

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
        val ringWidth = max(r * 0.10f, minStroke)         // ضخامت رینگ بیرونی
        val innerRingWidth = max(r * 0.03f, minStroke)    // رینگ داخلی ظریف
        val zThickness = max(r * 0.22f, 3.dp.toPx())      // ضخامت پیکسل Z
        val zStroke = max(zThickness * 0.18f, 1.5.dp.toPx())

        // 1) پس‌زمینه‌ی اُرب با گرادیان شعاعی (عمق و حس حجمی)
        val orbBrush = Brush.radialGradient(
            colors = listOf(
                baseColor.copy(alpha = 0.95f),
                baseColor.copy(alpha = 1f),
            ),
            center = center,
            radius = r
        )
        drawCircle(brush = orbBrush, radius = r, center = center)

        // 2) رینگ بیرونی براق با گرادیان چرخشی (نمای لوکس)
        inset(ringWidth / 2f) {
            rotate(if (animateRing) rotation else 25f) {
                val sweep = Brush.sweepGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.30f),
                        accentColor.copy(alpha = 0.20f),
                        Color.Transparent,
                        accentColor.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.30f)
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

        // 3) هاله‌ی داخلی خیلی ظریف برای جدا کردن Z از پس‌زمینه
        drawCircle(
            color = Color.White.copy(alpha = 0.08f),
            radius = r - ringWidth - innerRingWidth / 2f,
            style = Stroke(width = innerRingWidth)
        )

        // 4) ساخت Z ضخیم (بلوک‌محور: نوار بالا + مورب ضخیم + نوار پایین)
        inset(ringWidth + innerRingWidth + max(1f, zStroke)) {
            val w = size.width
            val h = size.height

            // باکس کلی حرف
            val left = w * 0.20f
            val right = w * 0.80f
            val top = h * 0.24f
            val bottom = h * 0.76f
            val width = right - left

            // نوار بالایی (مستطیل ضخیم)
            val topBar = Path().apply {
                addRect(Rect(left, top, right, top + zThickness))
            }

            // نوار پایینی
            val bottomBar = Path().apply {
                addRect(Rect(left, bottom - zThickness, right, bottom))
            }

            // مورب ضخیم به‌صورت "چهارضلعی با ضخامت واقعی"
            // مسیر مرکزی مورب: از گوشه‌ی راستِ نوار بالا به گوشه‌ی چپِ نوار پایین
            val p1 = Offset(right - zThickness * 0.15f, top + zThickness) // کمی داخل‌تر تا اتصال‌ها تمیز بشن
            val p2 = Offset(left + zThickness * 0.15f, bottom - zThickness)
            val vx = p2.x - p1.x
            val vy = p2.y - p1.y
            val len = sqrt((vx * vx + vy * vy).toDouble()).toFloat().coerceAtLeast(1e-3f)
            val nx = -vy / len // نرمال واحد
            val ny = vx / len
            val ox = nx * (zThickness / 2f)
            val oy = ny * (zThickness / 2f)

            val diag = Path().apply {
                // چهار گوشه‌ی موازی‌با‌مورب
                moveTo(p1.x + ox, p1.y + oy)
                lineTo(p2.x + ox, p2.y + oy)
                lineTo(p2.x - ox, p2.y - oy)
                lineTo(p1.x - ox, p1.y - oy)
                close()
            }

            // گرادیان پرکننده‌ی Z (مورب از روشن به تیره)
            val zFill = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.90f),
                    onBase.copy(alpha = 0.95f),
                    onBase.copy(alpha = 0.80f),
                ),
                start = Offset(left, top),
                end = Offset(right, bottom)
            )

            // کانتور Z برای تفکیک بهتر
            val zEdge = onBase.copy(alpha = 0.25f)

            // رسم: ابتدا پر، سپس کانتور
            drawPath(topBar, brush = zFill)
            drawPath(diag, brush = zFill)
            drawPath(bottomBar, brush = zFill)

            drawPath(topBar, color = zEdge, style = Stroke(width = zStroke, join = StrokeJoin.Round, cap = StrokeCap.Round))
            drawPath(diag, color = zEdge, style = Stroke(width = zStroke, join = StrokeJoin.Round, cap = StrokeCap.Round))
            drawPath(bottomBar, color = zEdge, style = Stroke(width = zStroke, join = StrokeJoin.Round, cap = StrokeCap.Round))

            // هایلایت نرم روی Z برای حس شیشه/فلز
            val highlight = Brush.linearGradient(
                colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                start = Offset(left, top),
                end = Offset(right, top + width * 0.35f)
            )
            drawPath(topBar, brush = highlight)
        }
    }
}
