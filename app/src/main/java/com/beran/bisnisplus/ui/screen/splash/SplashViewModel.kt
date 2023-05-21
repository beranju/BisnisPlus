package com.beran.bisnisplus.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.beran.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.Flow

class SplashViewModel(private val useCase: AuthUseCase) : ViewModel() {
    val isLogin: Boolean = useCase.isLogin()
    val isFirst: Flow<Boolean> = useCase.showOnBoard()
}