package com.msa.finhub.feature.inquiry

enum class FieldType { Text, Bool }

data class FieldSpec(
    val key: String,
    val label: String,
    val type: FieldType = FieldType.Text,
    val hint: String? = null,
    val required: Boolean = true,
    val defaultText: String = "",
    val defaultBool: Boolean = false
)

data class EndpointSpec(
    val route: String,   // برای ناوبری
    val title: String,   // عنوان UI
    val path: String,    // مسیر API (نسبت به baseUrl)
    val fields: List<FieldSpec>
)

object InquirySpecs {

    // 1) استعلام چک صیاد
    val SayadChequeInquiry = EndpointSpec(
        route = "sayadChequeInquiry",
        title = "استعلام چک صیاد",
        path = "/api/v1/FinnoTech/sayadChequeInquiry",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("sayadId", "شناسه چک صیادی"),
            FieldSpec("customerNationalCode", "کد ملی مشتری"),
            FieldSpec("customerCode", "کد مشتری"),
            FieldSpec("isOwnerByCustomer", "چک برای خود مشتری؟", FieldType.Bool, required = false)
        )
    )

    // 2) استعلام رنگ چک حقیقی
    val SayadCheckColorStatus = EndpointSpec(
        route = "sayadCheckColorStatus",
        title = "استعلام رنگ چک (حقیقی)",
        path = "/api/v1/FinnoTech/SayadCheckColorStatus",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("sayadId", "شناسه چک صیادی"),
            FieldSpec("customerNationalId", "کد ملی مشتری"),
            FieldSpec("customerCode", "کد مشتری"),
            FieldSpec("isOwnerByCustomer", "چک برای خود مشتری؟", FieldType.Bool, required = false)
        )
    )

    // 3) استعلام رنگ چک حقوقی
    val SayadCheckColorLegalStatus = EndpointSpec(
        route = "sayadCheckColorLegalStatus",
        title = "استعلام رنگ چک (حقوقی)",
        path = "/api/v1/FinnoTech/SayadCheckColorLegalStatus",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("sayadId", "شناسه چک صیادی"),
            FieldSpec("customerNationalId", "کد ملی مشتری"),
            FieldSpec("customerCode", "کد مشتری"),
            FieldSpec("isOwnerByCustomer", "چک برای خود مشتری؟", FieldType.Bool, required = false)
        )
    )

    // 4) احراز هویت حقیقی (مسیر همین است)
    val PersonalIdentity = EndpointSpec(
        route = "personalIdentity",
        title = "احراز هویت حقیقی (فردی)",
        path = "/PersonalIdentity",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("NationalCode", "کد ملی فرد"),
            FieldSpec("BirthDate", "تاریخ تولد", hint = "مثال: 1377/05/29")
        )
    )

    // 5) تطابق کدملی و شِبا/چک
    val NationalCodeSayadIdIdentity = EndpointSpec(
        route = "nationalCodeSayadIdIdentity",
        title = "تطابق کدملی و شِبا/چک",
        path = "/api/v1/FinnoTech/nationalCodeSayadIdIdentity",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("iban", "شماره شبا", hint = "IR..."),
            FieldSpec("customerCode", "کد مشتری"),
            FieldSpec("sayadId", "شناسه چک"),
            FieldSpec("isOwnerByCustomer", "چک برای خود مشتری؟", FieldType.Bool, required = false),
            FieldSpec("customerNationalId", "کد ملی مشتری")
        )
    )

    // 6) تطابق کدملی و موبایل
    val MobileNationalCode = EndpointSpec(
        route = "mobileNationalCode",
        title = "تطابق کدملی و موبایل",
        path = "/api/v1/FinnoTech/MobileNationalCode",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("mobile", "شماره موبایل", hint = "09..."),
            FieldSpec("nationalCode", "کد ملی")
        )
    )

    // 7) استعلام ضمانت‌های فرد
    val GuarantyInquiry = EndpointSpec(
        route = "guarantyInquiry",
        title = "استعلام ضمانت‌های فرد",
        path = "/api/v1/FinnoTech/guarantyInquiry",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("nid", "کد ملی")
        )
    )

    // 8) تأیید چک توسط گیرنده
    val SayadAcceptChequeReciever = EndpointSpec(
        route = "sayadAcceptChequeReciever",
        title = "تأیید چک توسط گیرنده",
        path = "/api/v1/FinnoTech/SayadAcceptChequeReciever",
        fields = listOf(
            FieldSpec("inquiry", "استعلام مجدد؟", FieldType.Bool, required = false),
            FieldSpec("sayadId", "شناسه چک صیادی"),
            FieldSpec("customerNationalCode", "کد ملی مشتری"),
            FieldSpec("customerCode", "کد مشتری"),
            FieldSpec("isOwnerByCustomer", "چک برای خود مشتری؟", FieldType.Bool, required = false)
        )
    )

    val all = listOf(
        SayadChequeInquiry,
        SayadCheckColorStatus,
        SayadCheckColorLegalStatus,
        PersonalIdentity,
        NationalCodeSayadIdIdentity,
        MobileNationalCode,
        GuarantyInquiry,
        SayadAcceptChequeReciever
    )
}
