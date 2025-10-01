package com.msa.finhub.feature.home.domain.model

data class DeviceSettings(
    val sayadAcceptChequeReceiverEnabled: Boolean,
    val guarantyInquiryEnabled: Boolean,
    val mobileNationalCodeEnabled: Boolean,
    val nationalCodeSayadIdIdentityEnabled: Boolean,
    val personalIdentityEnabled: Boolean,
    val sayadCheckColorLegalStatusEnabled: Boolean,
    val sayadChequeInquiryEnabled: Boolean,
    val sayadCheckColorStatusEnabled: Boolean,
)