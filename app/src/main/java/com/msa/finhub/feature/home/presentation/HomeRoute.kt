package com.msa.finhub.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state.value,
        onNavigate = { destination -> navController.navigate(destination) },
        onRetry = { viewModel.refresh() },
    )
}