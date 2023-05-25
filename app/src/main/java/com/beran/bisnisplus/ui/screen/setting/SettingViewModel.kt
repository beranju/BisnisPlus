package com.beran.bisnisplus.ui.screen.setting.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.model.UserModel
import com.beran.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    private var _user: MutableStateFlow<SettingState<UserModel>> =
        MutableStateFlow(SettingState.Loading)
    val user get() = _user.asStateFlow()

    private var _uiState: MutableStateFlow<SettingStates<Unit>> =
        MutableStateFlow(SettingStates.Initial)
    val uiState get() = _uiState.asStateFlow()

    fun fetchCurrentUser() {
        viewModelScope.launch {
            authUseCase.userDetail().collect { result ->
                when (result) {
                    is Resource.Loading -> _user.value = SettingState.Loading
                    is Resource.Error -> _user.value = SettingState.Error(result.message)
                    is Resource.Success -> _user.value = SettingState.Success(result.data)
                }
            }
        }
    }

    fun updateProfile(userModel: UserModel) {
        viewModelScope.launch {
            authUseCase.updateProfile(userModel).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = SettingStates.Loading
                    is Resource.Success -> _uiState.value = SettingStates.Success(result.data)
                    is Resource.Error -> _uiState.value = SettingStates.Error(result.message)
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCase.logOut()
        }
    }
}