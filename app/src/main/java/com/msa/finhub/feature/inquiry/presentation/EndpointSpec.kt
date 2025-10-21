package com.msa.finhub.feature.inquiry.presentation

import androidx.annotation.StringRes
import com.msa.finhub.R


enum class FieldType { Text, Bool }

data class FieldSpec(
    val key: String,
    @StringRes val labelRes: Int,
    val type: FieldType = FieldType.Text,
    @StringRes val hintRes: Int? = null,
    val required: Boolean = true,
    val defaultText: String = "",
    val defaultBool: Boolean = false
)

data class InquiryHelp(
    @StringRes val headlineRes: Int,
    @StringRes val descriptionRes: Int
)


data class EndpointSpec(
    val route: String,   // برای ناوبری
    @StringRes val titleRes: Int,   // عنوان UI
    val path: String,    // مسیر API (نسبت به baseUrl)
    val fields: List<FieldSpec>,
    val help: InquiryHelp? = null
)

object InquirySpecs {


    // 1) استعلام چک صیاد
    val SayadChequeInquiry = EndpointSpec(
        route = "sayadChequeInquiry",
        titleRes = R.string.home_sayad_cheque_inquiry,
        path = "/api/v1/FinnoTech/sayadChequeInquiry",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("sayadId", R.string.inquiry_field_sayad_id),
            FieldSpec("customerNationalCode", R.string.inquiry_field_customer_national_code, required = false),
            FieldSpec("customerCode", R.string.inquiry_field_customer_code, required = false),
            FieldSpec("isOwnerByCustomer", R.string.inquiry_field_is_owner_by_customer, FieldType.Bool, required = false)
        ),
        help = InquiryHelp(
            headlineRes = R.string.inquiry_help_sayad_cheque_title,
            descriptionRes = R.string.inquiry_help_sayad_cheque_body
        )
    )

    // 2) استعلام رنگ چک حقیقی
    val SayadCheckColorStatus = EndpointSpec(
        route = "sayadCheckColorStatus",
        titleRes = R.string.home_sayad_check_color_status,
        path = "/api/v1/FinnoTech/SayadCheckColorStatus",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("sayadId", R.string.inquiry_field_sayad_id, required = false),
            FieldSpec("customerNationalId", R.string.inquiry_field_customer_national_code),
            FieldSpec("customerCode", R.string.inquiry_field_customer_code, required = false),
            FieldSpec("isOwnerByCustomer", R.string.inquiry_field_is_owner_by_customer, FieldType.Bool, required = false)
        ),
        help = InquiryHelp(
            headlineRes = R.string.inquiry_help_color_status_person_title,
            descriptionRes = R.string.inquiry_help_color_status_person_body
        )
    )

    // 3) استعلام رنگ چک حقوقی
    val SayadCheckColorLegalStatus = EndpointSpec(
        route = "sayadCheckColorLegalStatus",
        titleRes = R.string.home_sayad_check_color_legal_status,
        path = "/api/v1/FinnoTech/SayadCheckColorLegalStatus",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("sayadId", R.string.inquiry_field_sayad_id, required = false),
            FieldSpec("customerNationalId", R.string.inquiry_field_economic_code),
            FieldSpec("customerCode", R.string.inquiry_field_customer_code, required = false),
            FieldSpec("isOwnerByCustomer", R.string.inquiry_field_is_owner_by_customer, FieldType.Bool, required = false)
        ),
        help = InquiryHelp(
            headlineRes = R.string.inquiry_help_color_status_legal_title,
            descriptionRes = R.string.inquiry_help_color_status_legal_body
        )
    )

    // 4) احراز هویت حقیقی (مسیر همین است)
    val PersonalIdentity = EndpointSpec(
        route = "personalIdentity",
        titleRes = R.string.home_personal_identity,
        path = "/api/v1/FinnoTech/PersonalIdentity",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("NationalCode", R.string.inquiry_field_person_national_code),
            FieldSpec(
                key = "BirthDate",
                labelRes = R.string.inquiry_field_birth_date,
                hintRes = R.string.inquiry_hint_birth_date
            )
        ),
        help = InquiryHelp(
            headlineRes = R.string.inquiry_help_personal_identity_title,
            descriptionRes = R.string.inquiry_help_personal_identity_body
        )
    )

    // 5) تطابق کدملی و شِبا/چک
    val NationalCodeSayadIdIdentity = EndpointSpec(
        route = "nationalCodeSayadIdIdentity",
        titleRes = R.string.home_national_code_sayad_id_identity,
        path = "/api/v1/FinnoTech/nationalCodeSayadIdIdentity",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("iban", R.string.inquiry_field_iban, hintRes = R.string.inquiry_hint_iban),
            FieldSpec("customerCode", R.string.inquiry_field_customer_code, required = false),
            FieldSpec("sayadId", R.string.inquiry_field_cheque_id, required = false),
            FieldSpec("isOwnerByCustomer", R.string.inquiry_field_is_owner_by_customer, FieldType.Bool, required = false),
            FieldSpec("customerNationalId", R.string.inquiry_field_customer_national_code)
        ),
        help = InquiryHelp(
            headlineRes = R.string.inquiry_help_national_code_iban_title,
            descriptionRes = R.string.inquiry_help_national_code_iban_body
        )
    )

    // 6) تطابق کدملی و موبایل
    val MobileNationalCode = EndpointSpec(
        route = "mobileNationalCode",
        titleRes = R.string.home_mobile_national_code,
        path = "/api/v1/FinnoTech/MobileNationalCode",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("mobile", R.string.inquiry_field_mobile, hintRes = R.string.inquiry_hint_mobile),
            FieldSpec("nationalCode", R.string.inquiry_field_national_code)
        ),
        help = InquiryHelp(
            headlineRes = R.string.inquiry_help_mobile_title,
            descriptionRes = R.string.inquiry_help_mobile_body
        )
    )

    // 7) استعلام ضمانت‌های فرد
    val GuarantyInquiry = EndpointSpec(
        route = "guarantyInquiry",
        titleRes = R.string.home_guaranty_inquiry,
        path = "/api/v1/FinnoTech/guarantyInquiry",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("nid", R.string.inquiry_field_national_code)
        ),
        help = InquiryHelp(
            headlineRes = R.string.inquiry_help_guaranty_title,
            descriptionRes = R.string.inquiry_help_guaranty_body
        )
    )

    // 8) تأیید چک توسط گیرنده
    val SayadAcceptChequeReciever = EndpointSpec(
        route = "sayadAcceptChequeReciever",
        titleRes = R.string.home_sayad_accept_cheque_receiver,
        path = "/api/v1/FinnoTech/SayadAcceptChequeReciever",
        fields = listOf(
            FieldSpec("inquiry", R.string.inquiry_field_retry, FieldType.Bool, required = false),
            FieldSpec("sayadId", R.string.inquiry_field_sayad_id),
            FieldSpec("customerNationalCode", R.string.inquiry_field_customer_national_code, required = false),
            FieldSpec("customerCode", R.string.inquiry_field_customer_code, required = false),
            FieldSpec("isOwnerByCustomer", R.string.inquiry_field_is_owner_by_customer, FieldType.Bool, required = false)
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
