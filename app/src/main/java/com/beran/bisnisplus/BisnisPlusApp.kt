package com.beran.bisnisplus

import android.app.Activity.RESULT_OK
import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.beran.bisnisplus.di.authUseCaseModule
import com.beran.bisnisplus.di.viewModelModule
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
import com.beran.bisnisplus.ui.screen.auth.signUp.SignUpViewModel
import com.beran.bisnisplus.ui.screen.auth.signin.SignInViewModel
import com.beran.bisnisplus.ui.screen.pembayaran.CreateNewPaymentScreen
import com.beran.bisnisplus.ui.screen.pembukuan.CreateNewRecordScreen
import com.beran.bisnisplus.ui.screen.pembukuan.component.FinancialStatementScreen
import com.beran.bisnisplus.ui.screen.setting.EditProfileUserScreen
import com.beran.bisnisplus.ui.screen.setting.SettingViewModel
import com.beran.bisnisplus.ui.screen.splash.SplashViewModel
import com.beran.core.di.authModule
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin
import timber.log.Timber

class BisnisPlusApk : Application() {
    override fun onCreate() {
        super.onCreate()
        // ** timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // ** koin
        startKoin {
            AndroidLogger()
            androidContext(this@BisnisPlusApk)
            modules(
                listOf(
                    authModule,
                    authUseCaseModule,
                    viewModelModule,
                )
            )
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
                val viewModel = koinViewModel<SplashViewModel>()
                SplashScreen(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        navController.popBackStack()
                        navController.navigate(Screen.Home.route){
                            popUpTo(Screen.Home.route){
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToOnBoard = {
                        navController.popBackStack()
                        navController.navigate(Screen.OnBoard.route){
                            popUpTo(Screen.OnBoard.route){
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(route = Screen.Home.route) {
                HomeScreen()
            }
            composable(route = Screen.OnBoard.route) {
                OnBoardingScreen(navController = navController)
            }
            composable(route = Screen.SignUp.route) {
                val viewModel = koinViewModel<SignUpViewModel>()
                val scope = rememberCoroutineScope()
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        scope.launch {
                            if (result.resultCode == RESULT_OK) {
                                viewModel.oneTapSignUp(result.data ?: return@launch)
                            }
                        }
                    }
                )
                SignUpScreen(
                    viewModel = viewModel,
                    navigateToSignDataBisnis = { name, email, password ->
                        navController.popBackStack()
                        navController.navigate(Screen.SignDataBisnis.route) {
                            popUpTo(Screen.SignDataBisnis.route) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToSignIn = {
                        navController.popBackStack()
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    },
                    oneTapLogin = {
                        scope.launch {
                            val signUpIntent = viewModel.getSignUpIntent()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signUpIntent ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
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
                val viewModel = koinViewModel<SignInViewModel>()
                val scope = rememberCoroutineScope()
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        scope.launch {
                            if (result.resultCode == RESULT_OK) {
                                viewModel.oneTapSignIn(result.data ?: return@launch)
                            }
                        }
                    }
                )
                LogInScreen(viewModel = viewModel, oneTapSignIn = {
                    scope.launch {
                        val signInIntent = viewModel.getSignInIntent()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntent ?: return@launch
                            ).build()
                        )

                    }

                }, onNavigateToHome = {
                    navController.navigate(Screen.Home.route)
                })
            }
            composable(route = Screen.Pembukuan.route) {
                PembukuanScreen(
                    onNavigateToCreateBook = { navController.navigate(Screen.CreateNewRecord.route) },
                    onNavigateToLaporanScreen = { route ->
                        navController.navigate(route)
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
                val viewModel = koinViewModel<SettingViewModel>()
                SettingScreen(
                    viewModel = viewModel,
                    onNavigateToEditProfile = {
                        navController.navigate(Screen.EditProfileUser.route)
                    },
                    signOut = {
                        viewModel.signOut()
                        navController.popBackStack()
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(Screen.SignIn.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(route = Screen.EditProfileUser.route) {
                EditProfileUserScreen(onNavigateBack = { navController.navigateUp() })
            }
            composable(route = Screen.CreateNewRecord.route) {
                CreateNewRecordScreen()
            }
            composable(route = Screen.CreateNewPayment.route) {
                CreateNewPaymentScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(route = Screen.FinancialStatement.route) {
                FinancialStatementScreen(onNavigateBack = {
                    navController.navigateUp()
                })
            }
        }
    }
}