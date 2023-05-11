package com.beran.bisnisplus.di

import com.beran.bisnisplus.ui.screen.auth.signUp.SignUpViewModel
import com.beran.bisnisplus.ui.screen.auth.signin.SignInViewModel
import com.beran.bisnisplus.ui.screen.setting.SettingViewModel
import com.beran.bisnisplus.ui.screen.splash.SplashViewModel
import com.beran.core.domain.usecase.AuthInteractor
import com.beran.core.domain.usecase.AuthUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authUseCaseModule = module {
    factory<AuthUseCase> { AuthInteractor(get()) }
}

val viewModelModule = module {
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SettingViewModel(get()) }
}