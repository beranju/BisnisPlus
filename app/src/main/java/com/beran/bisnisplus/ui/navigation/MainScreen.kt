package com.beran.bisnisplus.ui.navigation

sealed class MainScreen(val route: String) {
    object Home : Screen(route = "home")

    object Pembukuan : Screen(route = "pembukuan")
    object CreateNewRecord : Screen(route = "createNewRecord")
    object FinancialStatement : Screen(route = "financialStatement")
    object EditBookRecord : Screen(route = "pembukuan/{bookId}"){
        fun createRoute(bookId: String) = "pembukuan/$bookId"
    }

    object Statistik : Screen("statistik")

    object Pembayaran : Screen("pembayaran")
    object CreateNewPayment : Screen(route = "createNewPayment")

    object Setting : Screen("setting")
    object EditProfileUser : Screen("editProfileUser")
}