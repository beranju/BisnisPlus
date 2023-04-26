package com.beran.bisnisplus.ui.component

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.beran.bisnisplus.ui.navigation.NavigationItem
import com.beran.bisnisplus.ui.navigation.Screen

@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val navigationItem = listOf(
        NavigationItem(title = "Home", icon = Icons.Outlined.Home, screen = Screen.Home),
        NavigationItem(
            title = "Buku",
            icon = Icons.Outlined.ListAlt,
            screen = Screen.Pembukuan
        ),
        NavigationItem(
            title = "Statistik",
            icon = Icons.Outlined.Analytics,
            screen = Screen.Statistik
        ),
        NavigationItem(
            title = "Tagihan",
            icon = Icons.Outlined.CreditCard,
            screen = Screen.Pembayaran
        ),
        NavigationItem(title = "Setting", icon = Icons.Outlined.Settings, screen = Screen.Setting),
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        navigationItem.map { screen ->
            BottomNavigationItem(
                selected = currentDestination == screen.screen.route,
                label = { Text(text = screen.title, style = MaterialTheme.typography.labelSmall) },
                alwaysShowLabel = currentDestination == screen.screen.route,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                },
                onClick = {
                    navController.navigate(screen.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}