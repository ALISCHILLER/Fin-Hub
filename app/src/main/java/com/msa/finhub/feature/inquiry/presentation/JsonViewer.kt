package com.msa.finhub.feature.inquiry.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
    "hasError" to "وضعیت خطا",
    "deathStatus" to "وضعیت حیات",
    "officeName" to "آدرس",
    "responseCode" to "کد پاسخ",
    "trackId" to "شناسه رهگیری",
    "status" to "وضعیت",
    "message" to "پیام",
    "data" to "داده",
    "result" to "نتیجه",
    "referenceId" to "شناسه مرجع",
    "sayadId" to "شناسه چک صیادی",
    "branchCode" to "کد شعبه",
    "bankCode" to "کد بانک",
    "amount" to "مبلغ",
    "dueDate" to "تاریخ سررسید",
    "description" to "توضیحات",
    "serialNo" to "شماره سریال",
    "seriesNo" to "شماره سری",
    "fromIban" to "از شبا",
    "reason" to "دلیل",
    "currency" to "ارز",
    "chequeStatus" to "وضعیت چک",
    "holders" to "گیرندگان/صاحبان",
    "signers" to "امضاکنندگان",
    "name" to "نام",
    "idCode" to "کد شناسه",
    "idType" to "نوع شناسه",
    "legalStamp" to "مهر حقوقی",
    "firstName" to "نام",
    "lastName" to "نام خانوادگی",
    "birthDate" to "تاریخ تولد",
    "fatherName" to "نام پدر",
    "nationalId" to "کد ملی",
    "gender" to "جنسیت",
    "mobile" to "شماره موبایل",
    "isValid" to "اعتبار اطلاعات",
    "chequeColorName" to "نام رنگ چک",
    "chequeColor" to "رنگ چک"
)

@Composable
fun JsonViewer(element: JsonElement, modifier: Modifier = Modifier, indent: Int = 0) {
    when (element) {
        is JsonObject -> {
            Column(modifier = modifier.padding(start = indent.dp)) {
                element.forEach { (key, value) ->
                    JsonItem(
                        label = FIELD_LABELS[key] ?: key,
                        value = value,
                        indent = indent + 8
                    )
                }
            }
        }
        is JsonArray -> {
            Column(modifier = modifier.padding(start = indent.dp)) {
                element.forEach { item ->
                    JsonViewer(item, indent = indent + 8)
                }
            }
        }
        is JsonPrimitive -> {
            Text(
                text = element.content,
                modifier = modifier.padding(start = indent.dp, top = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun JsonItem(label: String, value: JsonElement, indent: Int) {
    when (value) {
        is JsonPrimitive -> {
            Row(modifier = Modifier.padding(start = indent.dp, top = 4.dp)) {
                Text(
                    text = "$label: ",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value.content,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        is JsonObject, is JsonArray -> {
            Column(modifier = Modifier.padding(start = indent.dp, top = 4.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                JsonViewer(value, indent = indent + 8)
            }
        }
    }
}
