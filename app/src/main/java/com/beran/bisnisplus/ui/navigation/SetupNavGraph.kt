package com.beran.bisnisplus.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.HomeScreen
import com.beran.bisnisplus.ui.screen.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(route = Screen.Splash.route){
            SplashScreen()
        }
        composable(route = Screen.Home.route){
            HomeScreen()
        }

    }

}