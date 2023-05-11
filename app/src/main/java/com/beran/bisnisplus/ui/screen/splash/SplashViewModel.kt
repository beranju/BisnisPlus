package com.beran.bisnisplus.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.beran.core.domain.usecase.AuthUseCase

class SplashViewModel(private val useCase: AuthUseCase): ViewModel() {
    val isLogin: Boolean = useCase.isLogin()
}