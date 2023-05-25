package com.beran.bisnisplus

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.beran.bisnisplus.ui.navigation.MainNavigation
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.OnBoardingScreen
import com.beran.bisnisplus.ui.screen.SplashScreen
import com.beran.bisnisplus.ui.screen.auth.LogInScreen
import com.beran.bisnisplus.ui.screen.auth.SetPhotoScreen
import com.beran.bisnisplus.ui.screen.auth.SignDataBisnis
import com.beran.bisnisplus.ui.screen.auth.SignUpScreen
import com.beran.bisnisplus.ui.screen.auth.dataBisnis.BisnisViewModel
import com.beran.bisnisplus.ui.screen.auth.signUp.SignUpViewModel
import com.beran.bisnisplus.ui.screen.auth.signin.SignInViewModel
import com.beran.bisnisplus.ui.screen.onboarding.OnBoardViewModel
import com.beran.bisnisplus.ui.screen.setting.SettingViewModel
import com.beran.bisnisplus.ui.screen.splash.SplashViewModel
import com.beran.core.domain.model.BusinessModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController, mainNavController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            val viewModel = koinViewModel<SplashViewModel>()
            SplashScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToOnBoard = {
                    navController.navigate(Screen.OnBoard.route) {
                        popUpTo(Screen.OnBoard.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screen.OnBoard.route) {
            val viewModel = koinViewModel<OnBoardViewModel>()
            OnBoardingScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.SignUp.route) {
            val viewModel = koinViewModel<SignUpViewModel>()
            val scope = rememberCoroutineScope()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    scope.launch {
                        if (result.resultCode == Activity.RESULT_OK) {
                            viewModel.oneTapSignUp(result.data ?: return@launch)
                        }
                    }
                }
            )
            SignUpScreen(
                viewModel = viewModel,
                navigateToSignDataBisnis = {
                    navController.navigate(Screen.SignDataBisnis.route) {
                        popUpTo(Screen.SignUp.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToSignIn = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
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
            val viewModel = koinViewModel<BisnisViewModel>()
            SignDataBisnis(
                viewmodel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.SignIn.route) {
                            inclusive = true
                        }
                    }
                },
                onCreateBisnisData = { bisnisName, bisnisCategory, commodity ->
                    val userId = viewModel.currentUser?.uid
                    val bisnisId = System.currentTimeMillis()
                    val bisnis = BusinessModel(
                        bisnisId = bisnisId.toString(),
                        bisnisName = bisnisName,
                        bisnisCategory = bisnisCategory,
                        commodity = commodity,
                        userId = userId
                    )
                    viewModel.createNewBisnis(bisnis)
                }
            )
        }
        composable(route = Screen.SetPhoto.route) {
            SetPhotoScreen(
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }
        composable(route = Screen.SignIn.route) {
            val viewModel = koinViewModel<SignInViewModel>()
            val scope = rememberCoroutineScope()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    scope.launch {
                        if (result.resultCode == Activity.RESULT_OK) {
                            viewModel.oneTapSignIn(result.data ?: return@launch)
                        }
                    }
                }
            )
            LogInScreen(
                viewModel = viewModel,
                oneTapSignIn = {
                    scope.launch {
                        val signInIntent = viewModel.getSignInIntent()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntent ?: return@launch
                            ).build()
                        )
                    }

                }, onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.SignIn.route) {
                            inclusive = true
                        }
                    }
                }, onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                })
        }
        composable(route = Screen.Main.route) {
            val viewModel = koinViewModel<SettingViewModel>()
            MainNavigation(
                navController = mainNavController,
                logOut = {
                    viewModel.signOut()
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo("main") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

