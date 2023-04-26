package com.beran.bisnisplus

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.beran.bisnisplus.ui.component.BottomBar
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.HomeScreen
import com.beran.bisnisplus.ui.screen.OnBoardingScreen
import com.beran.bisnisplus.ui.screen.PembukuanScreen
import com.beran.bisnisplus.ui.screen.SplashScreen
import com.beran.bisnisplus.ui.screen.auth.LogInScreen
import com.beran.bisnisplus.ui.screen.auth.SetPhotoScreen
import com.beran.bisnisplus.ui.screen.auth.SignDataBisnis
import com.beran.bisnisplus.ui.screen.auth.SignUpScreen

@Composable
fun BisnisPlusApp(navController: NavHostController, modifier: Modifier = Modifier) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            when (currentDestination) {
                Screen.Home.route -> CustomAppBar(titleAppBar = "Bisnis Plus", true)
                Screen.Pembukuan.route -> CustomAppBar(titleAppBar = "Pembukuan")
                Screen.Statistik.route -> CustomAppBar(titleAppBar = "Statistik")
                Screen.Pembayaran.route -> CustomAppBar(titleAppBar = "Pembayaran")
                Screen.Setting.route -> CustomAppBar(titleAppBar = "Setting")
            }
        },
        bottomBar = {
            if (currentDestination == Screen.Home.route || currentDestination == Screen.Pembukuan.route || currentDestination == Screen.Statistik.route || currentDestination == Screen.Pembayaran.route || currentDestination == Screen.Setting.route) {
                BottomBar(navController = navController)
            }
        }, modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Splash.route) {
                SplashScreen(navController)
            }
            composable(route = Screen.Home.route) {
                HomeScreen()
            }
            composable(route = Screen.OnBoard.route) {
                OnBoardingScreen(navController = navController)
            }
            composable(route = Screen.SignUp.route) {
                SignUpScreen(navigateToSignDataBisnis = {
                    navController.navigate(Screen.SignDataBisnis.route)
                })
            }
            composable(route = Screen.SignDataBisnis.route) {
                SignDataBisnis(onNavigateToSetPhoto = {
                    navController.navigate(Screen.SetPhoto.route)
                })
            }
            composable(route = Screen.SetPhoto.route) {
                SetPhotoScreen(onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route)
                })
            }
            composable(route = Screen.SignIn.route) {
                LogInScreen {
                    navController.navigate(Screen.Home.route)
                }
            }
            composable(route = Screen.Pembukuan.route) {
                PembukuanScreen()
            }
        }
    }
}