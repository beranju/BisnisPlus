package com.beran.bisnisplus.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

val navigationItem = listOf(
    NavigationItem(title = "Home", icon = Icons.Outlined.Home, screen = MainScreen.Home),
    NavigationItem(
        title = "Buku",
        icon = Icons.Outlined.ListAlt,
        screen = MainScreen.Pembukuan
    ),
    NavigationItem(
        title = "Statistik",
        icon = Icons.Outlined.Analytics,
        screen = MainScreen.Statistik
    ),
    NavigationItem(
        title = "Tagihan",
        icon = Icons.Outlined.CreditCard,
        screen = MainScreen.Pembayaran
    ),
    NavigationItem(
        title = "Setting",
        icon = Icons.Outlined.Settings,
        screen = MainScreen.Setting
    ),
)
