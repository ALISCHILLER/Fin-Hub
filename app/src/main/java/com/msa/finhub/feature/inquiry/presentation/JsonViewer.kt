package com.msa.finhub.feature.inquiry.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

private val FIELD_LABELS = mapOf(

    "amount" to "مبلغ",
    "bankCode" to "کد بانک",
    "benefitAmount" to "سود تجمیعی",
    "birthDate" to "تاریخ تولد",
    "blockStatus" to "وضعیت مسدودی",
    "branchCode" to "کد شعبه",
    "branchDescription" to "نام شعبه",
    "chequeColor" to "کد رنگ چک",
    "_chequeColor" to "کد رنگ چک (سیستم)",
    "chequeColorName" to "نام رنگ چک",
    "chequeMedia" to "نوع رسانه چک",
    "chequeStatus" to "وضعیت چک",
    "chequeType" to "نوع چک",
    "commitmentBalanceAmount" to "مانده تعهد",
    "currency" to "نوع ارز",
    "data" to "داده",
    "deathStatus" to "وضعیت حیات",
    "debtorFirstName" to "نام وام‌گیرنده",
    "debtorLastName" to "نام خانوادگی وام‌گیرنده",
    "debtorList" to "فهرست تسهیلات",
    "defunctAmount" to "مانده سوخت شده",
    "deferredAmount" to "مانده معوق",
    "dueDate" to "تاریخ سررسید",
    "endDate" to "تاریخ پایان",
    "error" to "خطا",
    "fatherName" to "نام پدر",
    "firstName" to "نام",
    "description" to "توضیحات",
    "fromIban" to "از شبا",
    "gender" to "جنسیت",
    "guaranteeStatus" to "وضعیت ضمانت",
    "guarantyFirstName" to "نام ضامن",
    "guarantyIdNumber" to "شماره ملی ضمانت‌شده",
    "guarantyLastName" to "نام خانوادگی ضامن",
    "guarantyLegalId" to "شناسه حقوقی ضمانت",
    "guarantyNationalCode" to "کد ملی ضامن",
    "guarantyPercent" to "درصد ضمانت",
    "hasError" to "وضعیت خطا",
    "holders" to "دارندگان چک",
    "idCode" to "کد شناسایی",
    "idType" to "نوع شناسه",
    "identityNo" to "شماره شناسنامه",
    "identitySeri" to "سری شناسنامه",
    "identitySerial" to "سریال شناسنامه",
    "inquiryResultId" to "شناسه نتیجه استعلام",
    "isValid" to "اعتبار اطلاعات",
    "issueDate" to "تاریخ ثبت اولیه",
    "latePenaltyAmount" to "جریمه دیرکرد",

    "legalStamp" to "مهر حقوقی",
    "locked" to "وضعیت قفل",
    "lockerBankCode" to "کد بانک لاک‌کننده",
    "lockerBranchCode" to "کد شعبه لاک‌کننده",
    "message" to "پیام",

    "mobile" to "شماره موبایل",
    "name" to "نام",
    "nationalId" to "کد ملی",
    "lastName" to "نام خانوادگی",
    "obligationAmount" to "مبلغ وجه التزام",
    "officeCode" to "کد اداره صادرکننده",
    "officeName" to "محل صدور",
    "operationStatus" to "وضعیت عملیات",
    "orginalAmount" to "اصل مبلغ تسهیلات",
    "pastExpiredAmount" to "مانده سررسید گذشته",
    "reason" to "دلیل",
    "referenceId" to "کد رهگیری",
    "requestNumber" to "شماره درخواست",
    "requestType" to "نوع درخواست",
    "result" to "نتیجه",
    "responseCode" to "کد پاسخ",
    "sayadId" to "شناسه صیاد",
    "serialNo" to "شماره سریال",
    "seriesNo" to "شماره سری",
    "setDate" to "تاریخ عقد قرارداد",
    "signers" to "امضاکنندگان",
    "status" to "وضعیت",
    "suspiciousAmount" to "مانده مشکوک‌الوصول",
    "toIban" to "به شبا",
    "totalAmount" to "مانده کلی بدهی",
    "trackId" to "کد پیگیری",
    "trackingCode" to "کد رهگیری ثبت احوال"
)

private val VALUE_LABELS: Map<String, Map<String, String>> = mapOf(
    "chequeStatus" to mapOf(
        "1" to "صادر شده",
        "2" to "نقد شده",
        "3" to "باطل شده",
        "4" to "برگشت خورده",
        "5" to "بخشی برگشت خورده",
        "6" to "در انتظار امضای ضامن",
        "7" to "در انتظار تایید گیرنده در کشیدن چک",
        "8" to "در انتظار تاییدیه گیرنده در انتقال چک"
    ),
    "chequeType" to mapOf(
        "1" to "عادی",
        "2" to "بانکی (تضمینی)",
        "3" to "رمزدار",
        "4" to "موردی"
    ),
    "chequeMedia" to mapOf(
        "1" to "چک کاغذی",
        "2" to "چک دیجیتال"
    ),
    "blockStatus" to mapOf(
        "0" to "چک مسدود نشده",
        "1" to "مسدودی موقت",
        "2" to "مسدودی دائم",
        "3" to "رفع مسدودی"
    ),
    "guaranteeStatus" to mapOf(
        "1" to "چک فاقد ضمانت است",
        "2" to "فرآیند ضمانت در جریان است",
        "3" to "فرآیند ضمانت ناتمام خاتمه یافته است",
        "4" to "تمام ضامن‌ها ضمانت کرده‌اند",
        "5" to "برخی ضامن‌ها ضمانت را رد کرده‌اند"
    ),
    "locked" to mapOf(
        "0" to "چک بدون محدودیت نقد کردن است",
        "1" to "چک لاک شده است"
    ),
    "chequeColor" to mapOf(
        "1" to "سفید (بدون سابقه برگشتی)",
        "2" to "زرد (یک فقره یا حداکثر ۵۰ میلیون ریال)",
        "3" to "نارنجی (۲ تا ۴ فقره یا حداکثر ۲۰۰ میلیون ریال)",
        "4" to "قهوه‌ای (۵ تا ۱۰ فقره یا حداکثر ۵۰۰ میلیون ریال)",
        "5" to "قرمز (بیش از ۱۰ فقره یا بیش از ۵۰۰ میلیون ریال)"
    ),
    "_chequeColor" to mapOf(
        "1" to "سفید (بدون سابقه برگشتی)",
        "2" to "زرد (یک فقره یا حداکثر ۵۰ میلیون ریال)",
        "3" to "نارنجی (۲ تا ۴ فقره یا حداکثر ۲۰۰ میلیون ریال)",
        "4" to "قهوه‌ای (۵ تا ۱۰ فقره یا حداکثر ۵۰۰ میلیون ریال)",
        "5" to "قرمز (بیش از ۱۰ فقره یا بیش از ۵۰۰ میلیون ریال)"
    ),
    "status" to mapOf(
        "DONE" to "فراخوانی موفق سرویس",
        "FAILED" to "فراخوانی ناموفق سرویس"
    ),
    "idType" to mapOf(
        "1" to "مشتری حقیقی",
        "2" to "مشتری حقوقی",
        "3" to "اتباع بیگانه حقیقی",
        "4" to "اتباع بیگانه حقوقی"
    ),
    "isValid" to mapOf(
        "yes" to "بله",
        "no" to "خیر",
        "true" to "بله",
        "false" to "خیر"
    )
)

@Composable
fun JsonViewer(element: JsonElement, modifier: Modifier = Modifier, indent: Int = 0) {
    JsonViewerInternal(element, modifier, indent, parentKey = null)
}

@Composable
private fun JsonViewerInternal(
    element: JsonElement,
    modifier: Modifier,
    indent: Int,
    parentKey: String?,
) {
    when (element) {
        is JsonObject -> {
            Column(
                modifier = modifier.padding(start = indent.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                element.forEach { (key, value) ->
                    JsonItem(
                        key = key,
                        label = FIELD_LABELS[key] ?: key,
                        value = value,
                        indent = indent
                    )
                }
            }
        }
        is JsonArray -> {
            Column(
                modifier = modifier.padding(start = indent.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                element.forEachIndexed { index, item ->
                    ArrayItemCard(index) {
                        JsonViewerInternal(
                            element = item,
                            modifier = Modifier.fillMaxWidth(),
                            indent = 0,
                            parentKey = parentKey
                        )
                    }
                }
            }
        }
        is JsonPrimitive -> {
            val label = parentKey?.let { FIELD_LABELS[it] ?: it }
            val formattedValue = formatPrimitive(parentKey, element)
            if (label != null) {
                PrimitiveItem(
                    label = label,
                    formattedValue = formattedValue,
                    indent = indent
                )
            } else {
                Text(
                    text = formattedValue.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = modifier.padding(start = indent.dp)
                )
            }
        }
    }
}

@Composable
private fun JsonItem(key: String, label: String, value: JsonElement, indent: Int) {
    when (value) {
        is JsonPrimitive -> {
            PrimitiveItem(
                label = label,
                formattedValue = formatPrimitive(key, value),
                indent = indent
            )
        }
        is JsonObject -> {
            SectionContainer(
                label = label,
                indent = indent
            ) {
                JsonViewerInternal(
                    element = value,
                    modifier = Modifier.fillMaxWidth(),
                    indent = 0,
                    parentKey = key
                )
            }
        }
        is JsonArray -> {
            SectionContainer(
                label = label,
                indent = indent
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "اطلاعاتی ثبت نشده است",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        value.forEachIndexed { index, item ->
                            ArrayItemCard(index) {
                                JsonViewerInternal(
                                    element = item,
                                    modifier = Modifier.fillMaxWidth(),
                                    indent = 0,
                                    parentKey = key
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun PrimitiveItem(label: String, formattedValue: FormattedValue, indent: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indent.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            shape = RoundedCornerShape(14.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = formattedValue.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                formattedValue.secondary?.let { secondary ->
                    AssistChip(
                        onClick = {},
                        enabled = false,
                        label = {
                            Text(
                                text = secondary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun SectionContainer(
    label: String,
    indent: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indent.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

@Composable
private fun ArrayItemCard(index: Int, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AssistChip(
                onClick = {},
                enabled = false,
                label = {
                    Text(
                        text = "آیتم ${index + 1}",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            content()
        }
    }
}

private data class FormattedValue(
    val primary: String,
    val secondary: String? = null
)

private fun formatPrimitive(key: String?, value: JsonPrimitive): FormattedValue {
    val content = value.content
    val mapped = key?.let { VALUE_LABELS[it]?.get(content) }
    return mapped?.let { FormattedValue(primary = it, secondary = content) } ?: FormattedValue(primary = content)
}
