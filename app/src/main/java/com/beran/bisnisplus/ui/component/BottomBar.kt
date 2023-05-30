package com.beran.bisnisplus.ui.component

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.beran.bisnisplus.ui.navigation.navigationItem

@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier = Modifier) {
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
                        restoreState = true
                        launchSingleTop = true
//                        popUpTo(Screen.Home.route) {
//                            saveState = true
//                        }
//                        restoreState = true
//                        launchSingleTop = true
                    }
                },
            )
        }
    }
}