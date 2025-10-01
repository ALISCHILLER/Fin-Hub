package com.msa.finhub.feature.home.data.mapper

import com.msa.finhub.feature.home.data.model.DeviceSettingsDto
import com.msa.finhub.feature.home.domain.model.DeviceSettings

fun DeviceSettingsDto.toDomain(): DeviceSettings = DeviceSettings(
    sayadAcceptChequeReceiverEnabled = enableSayadAcceptChequeReceiver.toBooleanOrFalse(),
    guarantyInquiryEnabled = enableGuarantyInquiry.toBooleanOrFalse(),
    mobileNationalCodeEnabled = enableMobileNationalCode.toBooleanOrFalse(),
    nationalCodeSayadIdIdentityEnabled = enableNationalCodeSayadIdIdentity.toBooleanOrFalse(),
    personalIdentityEnabled = enablePersonalIdentity.toBooleanOrFalse(),
    sayadCheckColorLegalStatusEnabled = enableSayadCheckColorLegalStatus.toBooleanOrFalse(),
    sayadChequeInquiryEnabled = enableSayadChequeInquiry.toBooleanOrFalse(),
    sayadCheckColorStatusEnabled = enableSayadCheckColorStatus.toBooleanOrFalse(),
)

private fun String?.toBooleanOrFalse(): Boolean = this?.equals("true", ignoreCase = true) == true