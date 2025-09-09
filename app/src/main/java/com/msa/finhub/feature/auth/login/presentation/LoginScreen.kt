package com.msa.finhub.feature.auth.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.msa.finhub.ui.components.ErrorDialog
import com.msa.finhub.ui.components.LoadingDialog
import com.msa.finhub.ui.components.LoadingOverlay

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
    onDismissError: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val onVar = cs.onSurfaceVariant
    val scroll = rememberScrollState()
    var passVisible by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(cs.background)
            .systemBarsPadding()
            .imePadding()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))
            Text("FinHub", style = MaterialTheme.typography.headlineLarge, color = cs.primary, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(6.dp))
            Text("به حساب کاری خود وارد شوید", color = onVar)

            Spacer(Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(18.dp),
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {

                    OutlinedTextField(
                        value = code,
                        onValueChange = onCodeChange,
                        label = { Text("کد پرسنلی") },
                        placeholder = { Text("مثال: M_mohamadkh") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Outlined.Badge, null) },
                        isError = state.error?.contains("کد پرسنلی") == true,
                        supportingText = {
                            state.error?.takeIf { it.contains("کد پرسنلی") }?.let { Text(it, color = cs.error) }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next, autoCorrect = false),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = cs.primary,
                            unfocusedBorderColor = cs.outline,
                            cursorColor = cs.primary,
                            focusedLabelColor = cs.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        label = { Text("رمز عبور") },
                        placeholder = { Text("******") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passVisible = !passVisible }) {
                                Icon(if (passVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility, null)
                            }
                        },
                        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done, autoCorrect = false),
                        keyboardActions = KeyboardActions (onDone = { onSubmit() }),
                        isError = state.error?.contains("رمز عبور") == true,
                        supportingText = {
                            state.error?.takeIf { it.contains("رمز عبور") }?.let { Text(it, color = cs.error) }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = cs.primary,
                            unfocusedBorderColor = cs.outline,
                            cursorColor = cs.primary,
                            focusedLabelColor = cs.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = rememberMe, onCheckedChange = onRememberChange)
                        Spacer(Modifier.width(6.dp))
                        Text("مرا به خاطر بسپار", color = onVar)
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { /* TODO */ }) { Text("فراموشی رمز؟") }
                    }

                    Spacer(Modifier.height(16.dp))

                    val enabled = code.isNotBlank() && password.length >= 6 && !state.isLoading
                    Button(
                        onClick = onSubmit,
                        enabled = enabled,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        if (state.hasSubmitted && state.isLoading) {
                            CircularProgressIndicator(strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(10.dp))
                            Text("در حال ورود…", fontWeight = FontWeight.SemiBold)
                        } else {
                            Text("ورود", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                        Text("با ورود، قوانین و حریم خصوصی را می‌پذیرید.", color = onVar, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} FinHub", style = MaterialTheme.typography.bodySmall, color = onVar)
            Spacer(Modifier.height(18.dp))
        }

        if (state.error?.isNotBlank() == true) {
            ErrorDialog(
                title = "خطا در ورود",
                message = state.error!!,
                onDismiss = onDismissError,     // دکمهٔ تایید هم به‌صورت پیش‌فرض همینو صدا می‌زنه
            )
        }

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
