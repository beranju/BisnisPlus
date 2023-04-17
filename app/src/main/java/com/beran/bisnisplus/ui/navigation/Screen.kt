package com.beran.bisnisplus.ui.navigation

sealed class Screen(val route: String){
    object Home: Screen("home")
    object Splash: Screen("splash")
    object OnBoard: Screen("onBoard")
    object SignUp: Screen("signUp")
    object SignDataBisnis: Screen("signDataBisnis")
    object SignIn: Screen("signIn")
}
