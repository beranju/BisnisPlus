package com.beran.bisnisplus.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.launch

class SettingViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    fun signOut() {
        viewModelScope.launch {
            authUseCase.logOut()
        }
    }
}