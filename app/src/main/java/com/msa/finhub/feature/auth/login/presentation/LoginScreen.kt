package com.msa.finhub.feature.auth.login.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.msa.finhub.core.datastore.DevPreferences
import com.msa.finhub.di.networkModule
import com.msa.finhub.feature.settings.presentation.SettingsEffect
import com.msa.finhub.ui.components.ErrorDialog
import com.msa.finhub.ui.components.LoadingDialog
import com.msa.finhub.ui.components.FinOutlinedTextField
import com.msa.finhub.ui.components.FinPasswordField
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

private const val DEV_TAP_THRESHOLD = 5
@Composable
fun LoginScreen(
    state: LoginUiState,
    code: String,
    password: String,
    rememberMe: Boolean,
    onCodeChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDismissError: () -> Unit,
    biometricAvailable: Boolean,
    onBiometricClick: () -> Unit


) {
    val cs = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    var tapCount by remember { mutableStateOf(0) }
    var showBaseDialog by remember { mutableStateOf(false) }
    var baseUrl by rememberSaveable { mutableStateOf("") }
    // ✅ تمام صفحه را RTL می‌کنیم — دیگر نیازی به Modifier.layoutDirection نیست
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cs.background)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(Modifier.height(48.dp))

                Text(
                    text = "FinHub",
                    style = typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = cs.primary,
                    modifier = Modifier
                        .clickable {
                            tapCount++
                            if (tapCount >= DEV_TAP_THRESHOLD) {
                                tapCount = 0
                                baseUrl = DevPreferences.getBaseUrl(context)
                                showBaseDialog = true
                            }
                        }
                )

                Spacer(Modifier.height(8.dp))

                // --- توضیح ورود (راست‌چین — به صورت پیش‌فرض در RTL) ---
                Text(
                    text = "به حساب کاری خود وارد شوید",
                    style = typography.titleMedium,
                    color = cs.onSurfaceVariant,
                    textAlign = TextAlign.End // ← تراز متن به صورت دستی برای اطمینان
                )

                Spacer(Modifier.height(32.dp))

                // --- کارت فرم ورود ---
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 2.dp,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        // --- فیلد کد پرسنلی ---
                        FinOutlinedTextField(
                            value = code,
                            onValueChange = onCodeChange,
                            label = "کد پرسنلی",
                            placeholder = "مثال: M_mohamadkh",
                            leading = { Icon(Icons.Outlined.Badge, contentDescription = null) },
                            isError = state.error?.contains("کد پرسنلی") == true,
                            supportingText = state.error?.takeIf { it.contains("کد پرسنلی") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,   // ⬅️ قبلاً Ascii بود
                                imeAction = ImeAction.Next,
                                autoCorrect = false
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        // --- فیلد رمز عبور ---
                        FinPasswordField(
                            value = password,
                            onValueChange = onPasswordChange,
                            leading = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                            placeholder = "••••••••",
                            isError = state.error?.contains("رمز عبور") == true,
                            supportingText = state.error?.takeIf { it.contains("رمز عبور") },
                            imeAction = ImeAction.Done,
                            onImeAction = onSubmit,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        // --- خط فراموشی رمز و مرا به خاطر بسپار ---
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = onRememberChange,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = cs.primary,
                                        uncheckedColor = cs.surfaceVariant, // ✅ رنگ پس‌زمینه چک‌باکس وقتی تیک نخورده
                                        checkmarkColor = cs.onPrimary      // ✅ رنگ تیک داخل چک‌باکس
                                    )
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "مرا به خاطر بسپار",
                                    style = typography.bodySmall,
                                    color = cs.onSurfaceVariant
                                )
                            }

                            TextButton(onClick = { /* TODO: Forgot password */ }) {
                                Text(
                                    text = "فراموشی رمز؟",
                                    style = typography.labelSmall,
                                    color = cs.primary
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // --- دکمه ورود ---
                        val isFormValid = code.isNotBlank() && password.length >= 6
                        val isSubmitting = state.hasSubmitted && state.isLoading
                        val isEnabled = isFormValid && !state.isLoading

                        Button(
                            onClick = onSubmit,
                            enabled = isEnabled,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isEnabled) cs.primary else cs.surfaceVariant,
                                contentColor = if (isEnabled) cs.onPrimary else cs.onSurfaceVariant
                            )
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.5.dp,
                                    color = cs.onPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                            }
                            Text(
                                text = if (isSubmitting) "در حال ورود…" else "ورود",
                                style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        if (biometricAvailable) {
                            TextButton(
                                onClick = onBiometricClick,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("ورود با بیومتریک")
                            }

                            Spacer(Modifier.height(12.dp))
                        }
                        // --- متن قوانین ---
                        ProvideTextStyle(typography.labelSmall) {
                            Text(
                                text = "با ورود، قوانین و حریم خصوصی را می‌پذیرید.",
                                color = cs.onSurfaceVariant.copy(alpha = 0.8f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // --- کپی‌رایت (چپ‌چین برای برند — با textAlign) ---
                Text(
                    text = "© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} FinHub   تولید و توسعه توسط گروه نرم افزار گروه صنعتی زر",
                    style = typography.labelSmall,
                    color = cs.onSurfaceVariant.copy(alpha = 0.6f),
                    textAlign = TextAlign.Start // ← در RTL, Start = چپ
                )

                Spacer(Modifier.height(24.dp))
            }

            if (showBaseDialog) {
                AlertDialog(
                    onDismissRequest = { showBaseDialog = false },
                    title = { Text("Base URL") },
                    text = {
                        OutlinedTextField(
                            value = baseUrl,
                            onValueChange = { baseUrl = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val url = baseUrl.trim()
                            DevPreferences.setBaseUrl(context, url)
                            unloadKoinModules(networkModule)
                            loadKoinModules(networkModule)
                            Toast.makeText(context, "آدرس ذخیره شد", Toast.LENGTH_SHORT).show()
                            showBaseDialog = false
                        }) {
                            Text("ذخیره")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showBaseDialog = false }) {
                            Text("انصراف")
                        }
                    }
                )
            }

            // --- دیالوگ خطا ---
            if (!state.isLoading && state.error?.isNotBlank() == true) {
                ErrorDialog(
                    title = "خطا در ورود",
                    message = state.error!!,
                    onDismiss = onDismissError
                )
            }

            // --- لودینگ مودال ---
            if (state.hasSubmitted && state.isLoading) {
                LoadingDialog(
                    show = true,
                    title = "در حال ورود",
                    text = "لطفاً صبر کنید…",
                    cancelable = false
                )
            }
        }
    }
}

