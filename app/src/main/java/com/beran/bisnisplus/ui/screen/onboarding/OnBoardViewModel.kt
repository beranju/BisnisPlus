package com.beran.bisnisplus.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.launch

class OnBoardViewModel(private val authUseCase: AuthUseCase): ViewModel() {
    fun setOnBoardState(isFirst: Boolean){
        viewModelScope.launch {
            authUseCase.setShowOnBoard(isFirst)
        }
    }
}