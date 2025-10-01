package com.msa.finhub.feature.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceSettingsDto(
    @SerialName("enableSayadAcceptChequeReciever")
    val enableSayadAcceptChequeReceiver: String? = null,
    @SerialName("enableGuarantyInquiry")
    val enableGuarantyInquiry: String? = null,
    @SerialName("enableMobileNationalCode")
    val enableMobileNationalCode: String? = null,
    @SerialName("enableNationalCodeSayadIdIdentity")
    val enableNationalCodeSayadIdIdentity: String? = null,
    @SerialName("enablePersonalIdentity")
    val enablePersonalIdentity: String? = null,
    @SerialName("enableSayadCheckColorLegalStatus")
    val enableSayadCheckColorLegalStatus: String? = null,
    @SerialName("enableSayadChequeInquiry")
    val enableSayadChequeInquiry: String? = null,
    @SerialName("enableSayadCheckColorStatus")
    val enableSayadCheckColorStatus: String? = null,
)

@Serializable
data class DeviceSettingsResponse(
    val hasError: Boolean,
    val data: DeviceSettingsDto? = null,
    val message: String? = null,
)