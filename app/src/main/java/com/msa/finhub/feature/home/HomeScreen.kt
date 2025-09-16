package com.msa.finhub.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.msa.finhub.feature.inquiry.presentation.InquirySpecs
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.msa.finhub.R
data class HomeItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val homeItems = listOf(
        HomeItem(
            stringResource(R.string.home_sayad_cheque_inquiry),
            InquirySpecs.SayadChequeInquiry.route,
            Icons.Outlined.Check
        ),
        HomeItem(
            stringResource(R.string.home_sayad_check_color_status),
            InquirySpecs.SayadCheckColorStatus.route,
            Icons.Outlined.VerifiedUser
        ),
        HomeItem(
            stringResource(R.string.home_sayad_check_color_legal_status),
            InquirySpecs.SayadCheckColorLegalStatus.route,
            Icons.Outlined.VerifiedUser
        ),
        HomeItem(
            stringResource(R.string.home_personal_identity),
            InquirySpecs.PersonalIdentity.route,
            Icons.Outlined.PersonSearch
        ),
        HomeItem(
            stringResource(R.string.home_national_code_sayad_id_identity),
            InquirySpecs.NationalCodeSayadIdIdentity.route,
            Icons.Outlined.VerifiedUser
        ),
        HomeItem(
            stringResource(R.string.home_mobile_national_code),
            InquirySpecs.MobileNationalCode.route,
            Icons.Outlined.VerifiedUser
        ),
        HomeItem(
            stringResource(R.string.home_guaranty_inquiry),
            InquirySpecs.GuarantyInquiry.route,
            Icons.Outlined.VerifiedUser
        ),
        HomeItem(
            stringResource(R.string.home_sayad_accept_cheque_receiver),
            InquirySpecs.SayadAcceptChequeReciever.route,
            Icons.Outlined.Check
        ),
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(homeItems, key = { it.route }) { item ->
                ElevatedCard(
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
