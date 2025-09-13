package com.msa.finhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.fragment.app.FragmentActivity
import com.msa.finhub.feature.auth.login.presentation.LoginScreen
import com.msa.finhub.feature.auth.login.presentation.LoginUiEvent
import com.msa.finhub.feature.auth.login.presentation.LoginUiState
import com.msa.finhub.feature.auth.login.presentation.LoginViewModel
import com.msa.finhub.nav.AppNav
import com.msa.finhub.ui.theme.FinHubTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinHubTheme {
                AppNav()

            }
        }
    }
}

