package com.msa.finhub.feature.auth.login.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import com.msa.finhub.ui.components.FinPasswordField

@Composable
fun LoginScreen(
    state: LoginUiState,
    onPersonelChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDismissError: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    var text by remember { mutableStateOf("") }
    Box(
        Modifier
            .fillMaxSize()
            .background(cs.background)       // سفید/روشن بر اساس تم
            .systemBarsPadding()
            .imePadding()
    ) {
        FinPasswordField(
            value = text,
            onValueChange = { text = it },
            label = "رمز عبور",
            placeholder = "******",
            leading = { Icon(Icons.Outlined.Lock, null) },
            isError = state.error?.contains("رمز عبور") == true,
            supportingText = state.error?.takeIf { it.contains("رمز عبور") },
            onImeAction = onSubmit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


/* ---- PREVIEWS ---- */
@Preview(name = "Login – Light", showBackground = true)
@Composable
private fun PreviewLoginLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        LoginScreen(
            state = LoginUiState(),
            onPersonelChange = {},
            onPasswordChange = {},
            onRememberChange = {},
            onSubmit = {},
            onDismissError = {}
        )
    }
}

@Preview(name = "Login – Dark")
@Composable
private fun PreviewLoginDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        LoginScreen(
            state = LoginUiState(isLoading = false),
            onPersonelChange = {},
            onPasswordChange = {},
            onRememberChange = {},
            onSubmit = {},
            onDismissError = {}
        )
    }
}
