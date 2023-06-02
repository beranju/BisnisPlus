package com.beran.bisnisplus.ui.component

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.BottomAppBar
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

    BottomAppBar(
        actions = {
            navigationItem.map { screen ->
                BottomNavigationItem(
                    selected = currentDestination == screen.screen.route,
                    label = {
                        Text(
                            text = screen.title.orEmpty(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    alwaysShowLabel = currentDestination == screen.screen.route,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    icon = {
                        screen.icon.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        screen.screen.route.let {
                            navController.navigate(it) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    },
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    )
}