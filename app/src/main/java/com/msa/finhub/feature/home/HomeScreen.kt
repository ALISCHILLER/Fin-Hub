package com.msa.finhub.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.msa.finhub.feature.inquiry.InquirySpecs

data class HomeItem(
    val title: String,
    val route: String,
    val icon: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val items = listOf(
        HomeItem("استعلام چک صیاد", InquirySpecs.SayadChequeInquiry.route) { Icon(Icons.Outlined.Check, null) },
        HomeItem("رنگ چک (حقیقی)", InquirySpecs.SayadCheckColorStatus.route) { Icon(Icons.Outlined.VerifiedUser, null) },
        HomeItem("رنگ چک (حقوقی)", InquirySpecs.SayadCheckColorLegalStatus.route) { Icon(Icons.Outlined.VerifiedUser, null) },
        HomeItem("احراز هویت حقیقی", InquirySpecs.PersonalIdentity.route) { Icon(Icons.Outlined.PersonSearch, null) },
        HomeItem("تطابق کدملی و شِبا/چک", InquirySpecs.NationalCodeSayadIdIdentity.route) { Icon(Icons.Outlined.VerifiedUser, null) },
        HomeItem("تطابق کدملی و موبایل", InquirySpecs.MobileNationalCode.route) { Icon(Icons.Outlined.VerifiedUser, null) },
        HomeItem("استعلام ضمانت‌های فرد", InquirySpecs.GuarantyInquiry.route) { Icon(Icons.Outlined.VerifiedUser, null) },
        HomeItem("تأیید چک توسط گیرنده", InquirySpecs.SayadAcceptChequeReciever.route) { Icon(Icons.Outlined.Check, null) },
    )


}
