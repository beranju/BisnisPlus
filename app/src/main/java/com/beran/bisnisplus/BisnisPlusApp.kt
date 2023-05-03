package com.beran.bisnisplus

import android.app.Application
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
import com.beran.bisnisplus.ui.screen.PembayaranScreen
import com.beran.bisnisplus.ui.screen.PembukuanScreen
import com.beran.bisnisplus.ui.screen.SettingScreen
import com.beran.bisnisplus.ui.screen.SplashScreen
import com.beran.bisnisplus.ui.screen.StatistikScreen
import com.beran.bisnisplus.ui.screen.auth.LogInScreen
import com.beran.bisnisplus.ui.screen.auth.SetPhotoScreen
import com.beran.bisnisplus.ui.screen.auth.SignDataBisnis
import com.beran.bisnisplus.ui.screen.auth.SignUpScreen
import com.beran.bisnisplus.ui.screen.pembayaran.CreateNewPaymentScreen
import com.beran.bisnisplus.ui.screen.pembukuan.CreateNewBookScreen
import com.beran.bisnisplus.ui.screen.setting.EditProfileUserScreen
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BisnisPlusApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // ** timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // ** koin
        startKoin {
            AndroidLogger()
            androidContext(this@BisnisPlusApp)
            modules(listOf())
        }
    }
}

@Composable
fun BisnisPlusApp(navController: NavHostController, modifier: Modifier = Modifier) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination?.route

    Scaffold(topBar = {
        when (currentDestination) {
            Screen.Home.route -> CustomAppBar(titleAppBar = "Bisnis Plus",
                showTrailingIcon = true,
                onLeadingClick = {})

            Screen.Pembukuan.route -> CustomAppBar(
                titleAppBar = "Pembukuan",
                onLeadingClick = {})

            Screen.Statistik.route -> CustomAppBar(
                titleAppBar = "Statistik",
                onLeadingClick = {})

            Screen.Pembayaran.route -> CustomAppBar(
                titleAppBar = "Pembayaran",
                onLeadingClick = {})

            Screen.Setting.route -> CustomAppBar(titleAppBar = "Setting", onLeadingClick = {})
        }
    }, bottomBar = {
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
                PembukuanScreen(
                    onNavigateToCreateBook = { navController.navigate(Screen.CreateNewBook.route) },
                    onNavigateToLaporanScreen = {route ->
                        // ** on navigate to laporan screen
                    }
                )
            }
            composable(route = Screen.Statistik.route) {
                StatistikScreen()
            }
            composable(route = Screen.Pembayaran.route) {
                PembayaranScreen(
                    onNavigateToCreateNewPayment = {
                        navController.navigate(Screen.CreateNewPayment.route)
                    }
                )
            }
            composable(route = Screen.Setting.route) {
                SettingScreen(onNavigateToEditProfile = {
                    navController.navigate(Screen.EditProfileUser.route)
                })
            }
            composable(route = Screen.EditProfileUser.route) {
                EditProfileUserScreen(onNavigateBack = { navController.navigateUp() })
            }
            composable(route = Screen.CreateNewBook.route) {
                CreateNewBookScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(route = Screen.CreateNewPayment.route) {
                CreateNewPaymentScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}