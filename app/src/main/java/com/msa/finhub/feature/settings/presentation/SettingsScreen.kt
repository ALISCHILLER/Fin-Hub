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

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ty = MaterialTheme.typography

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("تنظیمات", style = ty.headlineSmall.copy(fontWeight = FontWeight.Bold))

            // حساب کاربری
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("حساب کاربری", style = ty.titleMedium)
                    val username = state.username ?: "—"
                    Text("کاربر: $username", style = ty.bodyMedium, color = cs.onSurfaceVariant)
                }
            }

            // Remember me
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("ورود", style = ty.titleMedium)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("حفظ ورود", style = ty.bodyLarge)
                            Text(
                                if (state.hasSavedCredentials)
                                    "اطلاعات ورود شما ذخیره شده است."
                                else
                                    "در صورت فعال‌سازی، هنگام ورود ذخیره می‌شود.",
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
                    ) { Text("حذف اطلاعات ورود ذخیره‌شده") }
                }
            }

            // امنیت و بیومتریک
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("امنیت و بیومتریک", style = ty.titleMedium)

                    val canEnableBio = state.biometricAvailable && state.hasSavedCredentials
                    val helper = when {
                        !state.biometricAvailable -> "دستگاه شما از بیومتریک پشتیبانی نمی‌کند."
                        !state.hasSavedCredentials -> "برای فعال‌سازی بیومتریک، ابتدا «حفظ ورود» را روشن کنید."
                        else -> null
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("ورود با اثرانگشت / Face", style = ty.bodyLarge)
                            Text(
                                helper ?: if (state.biometricEnabled)
                                    "بیومتریک فعال است. برای ورود سریع از اثرانگشت/Face استفاده می‌شود."
                                else
                                    "با فعال‌سازی، بازیابی رمز تنها با بیومتریک ممکن می‌شود.",
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
                        ) { Text("راه‌اندازی بیومتریک") }
                    }
                }
            }

            // خروج
            ElevatedCard(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("منطقه حساس", style = ty.titleMedium, color = cs.error)
                    Button(
                        onClick = { onEvent(SettingsEvent.ClickLogout) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.error,
                            contentColor = cs.onError
                        )
                    ) { Text("خروج از حساب") }

                    Text(
                        "با خروج از حساب، فقط توکن پاک می‌شود؛ اگر «حفظ ورود» فعال باشد، اطلاعات ورود و بیومتریک باقی می‌مانند.",
                        style = ty.labelSmall, color = cs.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} FinHub",
                style = ty.labelSmall, color = cs.onSurfaceVariant)
        }

        // دیالوگ‌ها
        if (state.showLogoutConfirm) {
            AlertDialog(
                onDismissRequest = { onEvent(SettingsEvent.DismissLogout) },
                title = { Text("خروج از حساب") },
                text = { Text("می‌خواهید خارج شوید؟") },
                confirmButton = { TextButton(onClick = { onEvent(SettingsEvent.ConfirmLogout) }) { Text("بله، خروج") } },
                dismissButton = { TextButton(onClick = { onEvent(SettingsEvent.DismissLogout) }) { Text("انصراف") } }
            )
        }

        if (state.showClearCredsConfirm) {
            AlertDialog(
                onDismissRequest = { onEvent(SettingsEvent.DismissClearCreds) },
                title = { Text("حذف اطلاعات ورود") },
                text = { Text("اطلاعات ورود ذخیره‌شده پاک شود؟") },
                confirmButton = { TextButton(onClick = { onEvent(SettingsEvent.ConfirmClearCreds) }) { Text("بله، حذف") } },
                dismissButton = { TextButton(onClick = { onEvent(SettingsEvent.DismissClearCreds) }) { Text("انصراف") } }
            )
        }

        if (state.showDisableBiometricConfirm) {
            AlertDialog(
                onDismissRequest = { onEvent(SettingsEvent.DismissDisableBiometric) },
                title = { Text("خاموش کردن بیومتریک") },
                text = { Text("ورود با بیومتریک غیرفعال شود؟") },
                confirmButton = { TextButton(onClick = { onEvent(SettingsEvent.ConfirmDisableBiometric) }) { Text("بله، خاموش شود") } },
                dismissButton = { TextButton(onClick = { onEvent(SettingsEvent.DismissDisableBiometric) }) { Text("انصراف") } }
            )
        }

        if (state.error?.isNotBlank() == true) {
            ErrorDialog(title = "خطا", message = state.error, onDismiss = { onEvent(SettingsEvent.DismissError) })
        }

        LoadingOverlay(
            show = state.isLoggingOut || state.isBiometricBusy,
            text = if (state.isLoggingOut) "در حال خروج…" else "در حال تنظیم بیومتریک…",
            modifier = Modifier.fillMaxSize()
        )
    }
    }
}
