package com.msa.finhub.feature.home.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.msa.finhub.R

import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

import com.msa.finhub.feature.home.domain.model.DeviceSettings
import com.msa.finhub.feature.inquiry.presentation.InquirySpecs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onNavigate: (String) -> Unit,
    onRetry: () -> Unit,
) {
    val items = remember (state.permissions) {
        state.permissions?.let { permissions ->
            homeEntries.filter { entry -> entry.isEnabled(permissions) }
        } ?: emptyList()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {

                        Text(
                            text = state.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                        Button(onClick = onRetry) {
                            Text(text = stringResource(id = R.string.error_retry))
                        }
                    }
                }
            }

            items.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(id = R.string.home_no_permissions),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(items, key = { it.route }) { item ->
                        ElevatedCard(
                            onClick = { onNavigate(item.route) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp),
                                )
                                Text(
                                    text = stringResource(id = item.titleRes),
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
private data class HomeEntry(
    @StringRes val titleRes: Int,
    val route: String,
    val icon: ImageVector,
    val isEnabled: (DeviceSettings) -> Boolean,
)

private val homeEntries = listOf(
    HomeEntry(
        titleRes = R.string.home_sayad_cheque_inquiry,
        route = InquirySpecs.SayadChequeInquiry.route,
        icon = Icons.Outlined.Check,
        isEnabled = { it.sayadChequeInquiryEnabled },
    ),
    HomeEntry(
        titleRes = R.string.home_sayad_check_color_status,
        route = InquirySpecs.SayadCheckColorStatus.route,
        icon = Icons.Outlined.VerifiedUser,
        isEnabled = { it.sayadCheckColorStatusEnabled },
    ),
    HomeEntry(
        titleRes = R.string.home_sayad_check_color_legal_status,
        route = InquirySpecs.SayadCheckColorLegalStatus.route,
        icon = Icons.Outlined.VerifiedUser,
        isEnabled = { it.sayadCheckColorLegalStatusEnabled },
    ),
    HomeEntry(
        titleRes = R.string.home_personal_identity,
        route = InquirySpecs.PersonalIdentity.route,
        icon = Icons.Outlined.PersonSearch,
        isEnabled = { it.personalIdentityEnabled },
    ),
    HomeEntry(
        titleRes = R.string.home_national_code_sayad_id_identity,
        route = InquirySpecs.NationalCodeSayadIdIdentity.route,
        icon = Icons.Outlined.VerifiedUser,
        isEnabled = { it.nationalCodeSayadIdIdentityEnabled },
    ),
    HomeEntry(
        titleRes = R.string.home_mobile_national_code,
        route = InquirySpecs.MobileNationalCode.route,
        icon = Icons.Outlined.VerifiedUser,
        isEnabled = { it.mobileNationalCodeEnabled },
    ),
    HomeEntry(
        titleRes = R.string.home_guaranty_inquiry,
        route = InquirySpecs.GuarantyInquiry.route,
        icon = Icons.Outlined.VerifiedUser,
        isEnabled = { it.guarantyInquiryEnabled },
    ),
    HomeEntry(
        titleRes = R.string.home_sayad_accept_cheque_receiver,
        route = InquirySpecs.SayadAcceptChequeReciever.route,
        icon = Icons.Outlined.Check,
        isEnabled = { it.sayadAcceptChequeReceiverEnabled },
    ),
)