package com.msa.finhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.msa.finhub.feature.auth.login.presentation.LoginScreen
import com.msa.finhub.feature.auth.login.presentation.LoginUiState
import com.msa.finhub.ui.theme.FinHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinHubTheme {
                LoginScreen(
                    state = LoginUiState(personelCode = "", password = "", rememberMe = false, isLoading = true, error = null),
                    onPersonelChange = {},
                    onPasswordChange = {},
                    onRememberChange = {},
                    onSubmit = {},
                    onDismissError = {}
                )
            }
        }
    }
}

