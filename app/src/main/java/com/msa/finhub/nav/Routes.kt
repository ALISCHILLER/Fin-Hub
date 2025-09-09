// Routes.kt
package com.msa.finhub.nav

import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val Login = "login"
    const val Home = "home"
    const val Settings = "settings"
}

data class BottomItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0
)
