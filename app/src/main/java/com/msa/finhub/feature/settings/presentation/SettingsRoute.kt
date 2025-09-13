// feature/settings/presentation/SettingsRoute.kt
package com.msa.finhub.feature.settings.presentation

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.msa.finhub.core.datastore.BiometricCredentialsStore
import org.koin.compose.koinInject

@Composable
fun SettingsRoute(
    vm: SettingsViewModel = koinViewModel(),
    onLoggedOut: () -> Unit
) {
    // ✅ به‌جای get() از koinInject استفاده کن
    val bioStore: BiometricCredentialsStore = koinInject()

    // اگر lifecycle-runtime-compose نداری، می‌تونی collectAsState() استفاده کنی
    val state = vm.state.collectAsStateWithLifecycle().value

    val context = LocalContext.current
    val activity = remember(context) { context as FragmentActivity }
    val scope = rememberCoroutineScope()

    // تشخیص پشتیبانی بیومتریک
    LaunchedEffect(Unit) {
        val can = BiometricManager.from(context)
            .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
        vm.onEvent(SettingsEvent.SetBiometricAvailable(can))
    }

    // افکت‌ها
    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            when (eff) {
                is SettingsEffect.LoggedOut -> onLoggedOut()
                is SettingsEffect.Toast -> { /* Snackbar/Toast اختیاری */ }
                is SettingsEffect.RequestEnableBiometric -> {
                    scope.launch {
                        val ok = bioStore.encryptAndSave(
                            activity = activity,
                            username = eff.username,
                            password = eff.password
                        )
                        vm.onEvent(SettingsEvent.BiometricEnabledResult(ok))
                    }
                }
            }
        }
    }

    SettingsScreen(state = state, onEvent = vm::onEvent)
}
