// AppNav.kt
package com.msa.finhub.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msa.finhub.feature.auth.login.presentation.LoginRoute
import com.msa.finhub.feature.home.presentation.HomeScreen
import com.msa.finhub.feature.settings.presentation.SettingsRoute
import com.msa.finhub.feature.inquiry.presentation.InquiryScreen
import com.msa.finhub.feature.inquiry.presentation.InquirySpecs

@Composable
fun AppNav() {
    val navController = rememberNavController()

    val items = remember {
        listOf(
            BottomItem(Routes.Home, "خانه", Icons.Filled.Home, Icons.Outlined.Home),
            BottomItem(Routes.Settings, "تنظیمات", Icons.Filled.Settings, Icons.Outlined.Settings),
        )
    }
    val bottomRoutes = remember { items.map { it.route }.toSet() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomRoutes

    Scaffold(
      //  contentWindowInsets = WindowInsets(0, 10, 0, 0),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            AnimatedVisibility(visible = showBottomBar) {
                FinBottomBar(
                    items = items,
                    currentRoute = currentRoute,
                    onSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { inner ->
        NavHost(
            navController = navController,
            startDestination = Routes.Login,
            modifier = Modifier.padding(inner)
        ) {
            composable(Routes.Login) {
                LoginRoute(
                    onLoggedIn = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Login) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.Home)     { HomeScreen(navController) }

            composable(Routes.Settings) {
                SettingsRoute(
                    onLoggedOut = {
                        navController.navigate(Routes.Login) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // ثبت همهٔ مسیرهای استعلام
            InquirySpecs.all.forEach { spec ->
                composable(spec.route) {
                    InquiryScreen(spec = spec, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}

@Composable
private fun FinBottomBar(
    items: List<BottomItem>,
    currentRoute: String?,
    onSelected: (String) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .navigationBarsPadding()
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 6.dp,
            shadowElevation = 10.dp,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val selected = currentRoute?.let { backStackMatches(it, item.route) } == true
                    FinBottomItem(
                        item = item,
                        selected = selected,
                        onClick = { onSelected(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.FinBottomItem(
    item: BottomItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val indicatorAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "indicatorAlpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.92f,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "iconScale"
    )
    val pillPadding by animateDpAsState(
        targetValue = if (selected) 0.dp else 10.dp,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "pillPadding"
    )

    // گرادینت اندیکاتور (آلفا روی Color اعمال می‌شود، نه Brush)
    val indicatorBrush = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f * indicatorAlpha),
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f * indicatorAlpha)
        )
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .semantics { contentDescription = item.label },
        contentAlignment = Alignment.Center
    ) {
        // پس‌زمینهٔ قرص‌شکل انتخاب
        Box(
            Modifier
                .matchParentSize()
                .padding(pillPadding)
                .clip(RoundedCornerShape(16.dp))
                .background(indicatorBrush)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.badgeCount > 0) {
                BadgedBox(badge = { Badge { Text(item.badgeCount.toString()) } }) {
                    IconBox(item, selected, scale)
                }
            } else {
                IconBox(item, selected, scale)
            }

            AnimatedVisibility(visible = selected) {
                Spacer(Modifier.size(8.dp))
                Crossfade(targetState = item.label, label = "labelCrossfade") { label ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun IconBox(item: BottomItem, selected: Boolean, scale: Float) {
    Box(
        modifier = Modifier
            .size(if (selected) 28.dp else 26.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val icon: ImageVector = if (selected) item.selectedIcon else item.unselectedIcon
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxSize(scale)
        )
    }
}

private fun backStackMatches(current: String, target: String): Boolean {
    return current == target || current.startsWith("$target/")
}




