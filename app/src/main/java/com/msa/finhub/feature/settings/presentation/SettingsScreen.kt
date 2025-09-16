package com.msa.finhub.feature.settings.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.msa.finhub.ui.components.ErrorDialog
import com.msa.finhub.ui.components.LoadingOverlay
import androidx.compose.ui.res.stringResource
import com.msa.finhub.R
import androidx.compose.runtime.remember
import java.time.LocalDate

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ty = MaterialTheme.typography
    val currentYear = remember { LocalDate.now().year }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(R.string.settings_title), style = ty.headlineSmall.copy(fontWeight = FontWeight.Bold))

            // حساب کاربری
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.settings_account_header), style = ty.titleMedium)
                    val username = state.username ?: "—"
                    Text(stringResource(R.string.settings_user, username), style = ty.bodyMedium, color = cs.onSurfaceVariant)
                }
            }

            // Remember me
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.settings_login_header), style = ty.titleMedium)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(stringResource(R.string.settings_remember_login), style = ty.bodyLarge)
                            Text(
                                stringResource(
                                    if (state.hasSavedCredentials)
                                        R.string.settings_remember_description_saved
                                    else
                                        R.string.settings_remember_description_unsaved
                                ),
                                style = ty.bodySmall,
                                color = cs.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = state.hasSavedCredentials,
                            onCheckedChange = { onEvent(SettingsEvent.ToggleRemember(it)) }
                        )
                    }

                    OutlinedButton(
                        onClick = { onEvent(SettingsEvent.ClickClearCreds) },
                        enabled = state.hasSavedCredentials,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(stringResource(R.string.settings_clear_saved_login)) }
                }
            }

            // امنیت و بیومتریک
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.settings_security_header), style = ty.titleMedium)

                    val canEnableBio = state.biometricAvailable && state.hasSavedCredentials
                    val helper = when {
                        !state.biometricAvailable -> stringResource(R.string.settings_bio_not_supported)
                        !state.hasSavedCredentials -> stringResource(R.string.settings_bio_enable_hint)
                        else -> null
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(stringResource(R.string.settings_bio_login), style = ty.bodyLarge)
                            Text(
                                helper ?: stringResource(
                                    if (state.biometricEnabled)
                                        R.string.settings_bio_enabled_description
                                    else
                                        R.string.settings_bio_disabled_description
                                ),
                                style = ty.bodySmall,
                                color = cs.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = state.biometricEnabled,
                            enabled = canEnableBio && !state.isBiometricBusy,
                            onCheckedChange = { checked ->
                                if (checked) onEvent(SettingsEvent.ClickEnableBiometric)
                                else onEvent(SettingsEvent.ClickDisableBiometric)
                            }
                        )
                    }

                    if (canEnableBio && !state.biometricEnabled) {
                        OutlinedButton(
                            onClick = { onEvent(SettingsEvent.ClickEnableBiometric) },
                            enabled = !state.isBiometricBusy,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(stringResource(R.string.settings_bio_setup)) }
                    }
                }
            }

            // خروج
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.settings_danger_zone), style = ty.titleMedium, color = cs.error)
                    Button(
                        onClick = { onEvent(SettingsEvent.ClickLogout) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.error,
                            contentColor = cs.onError
                        )
                    ) {Text(stringResource(R.string.settings_logout)) }

                    Text(
                        stringResource(R.string.settings_footer, currentYear),
                        style = ty.labelSmall, color = cs.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

        }

        // دیالوگ‌ها
        if (state.showLogoutConfirm) {
            ConfirmDialog(
                title = stringResource(R.string.settings_logout),
                message = stringResource(R.string.dialog_logout_message),
                onConfirm = { onEvent(SettingsEvent.ConfirmLogout) },
                onDismiss = { onEvent(SettingsEvent.DismissLogout) }
            )
        }

        if (state.showClearCredsConfirm) {
            ConfirmDialog(
                title = stringResource(R.string.dialog_clear_creds_title),
                message = stringResource(R.string.dialog_clear_creds_message),
                onConfirm = { onEvent(SettingsEvent.ConfirmClearCreds) },
                onDismiss = { onEvent(SettingsEvent.DismissClearCreds) }
            )
        }

        if (state.showDisableBiometricConfirm) {
            ConfirmDialog(
                title = stringResource(R.string.dialog_disable_bio_title),
                message = stringResource(R.string.dialog_disable_bio_message),
                onConfirm = { onEvent(SettingsEvent.ConfirmDisableBiometric) },
                onDismiss = { onEvent(SettingsEvent.DismissDisableBiometric) }
            )
        }

        if (state.error?.isNotBlank() == true) {
            ErrorDialog(title = stringResource(R.string.error_title_general), message = state.error, onDismiss = { onEvent(SettingsEvent.DismissError) })
        }

        LoadingOverlay(
            show = state.isLoggingOut || state.isBiometricBusy,
            text = stringResource(if (state.isLoggingOut) R.string.loading_logging_out else R.string.loading_setting_biometric),
            modifier = Modifier.fillMaxSize()
        )
    }
    }
}
