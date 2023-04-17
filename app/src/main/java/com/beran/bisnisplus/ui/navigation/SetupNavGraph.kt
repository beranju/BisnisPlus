package com.beran.bisnisplus.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.HomeScreen
import com.beran.bisnisplus.ui.screen.OnBoardingScreen
import com.beran.bisnisplus.ui.screen.SplashScreen
import com.beran.bisnisplus.ui.screen.auth.SignUpScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(route = Screen.Splash.route){
            SplashScreen(navController)
        }
        composable(route = Screen.Home.route){
            HomeScreen()
        }
        composable(route = Screen.OnBoard.route){
            OnBoardingScreen(navController = navController)
        }
        composable(route = Screen.SignUp.route){
            SignUpScreen(navController = navController)
        }

    }

}