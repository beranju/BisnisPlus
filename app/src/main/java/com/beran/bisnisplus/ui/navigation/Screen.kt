package com.beran.bisnisplus.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object OnBoard : Screen("onBoard")
    object SignUp : Screen("signUp")
    object SignDataBisnis : Screen("signDataBisnis")
    object SetPhoto : Screen("setPhoto")
    object SignIn : Screen("signIn")
    object Home : Screen(route = "home")
    object Pembukuan : Screen(route = "pembukuan")
    object CreateNewBook : Screen(route = "createNewBook")
    object CreateNewRecord : Screen(route = "createNewRecord")
    object Statistik : Screen("statistik")
    object Pembayaran : Screen("pembayaran")
    object CreateNewPayment : Screen(route = "createNewPayment")
    object Setting : Screen("setting")
    object EditProfileUser : Screen("editProfileUser")

}
