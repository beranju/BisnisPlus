package com.beran.bisnisplus.ui.screen.setting.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.model.BusinessModel
import com.beran.core.domain.model.UserModel
import com.beran.core.domain.usecase.AuthUseCase
import com.beran.core.domain.usecase.bisnis.BisnisUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel(private val authUseCase: AuthUseCase, private val bisnisUseCase: BisnisUseCase) : ViewModel() {

    private var _state: MutableState<SettingState> = mutableStateOf(SettingState())
    val state: State<SettingState> get() = _state

    init {
        fetchCurrentUser()
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            authUseCase.userDetail().collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> _state.value =
                        _state.value.copy(isLoading = false,user = result.data, error = null)
                }
            }
        }
    }

    fun completeUserProfile(userModel: UserModel, businessModel: BusinessModel){
        viewModelScope.launch {
           withContext(Dispatchers.IO) { updateProfile(userModel) }
            bisnisUseCase.createNewBisnis(businessModel).collect{result ->
                when (result) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> {
                        _state.value =
                            _state.value.copy(isLoading = false, isSuccess = true, error = null)
                        fetchCurrentUser()
                    }
                }
            }
        }
    }

    fun updateProfile(userModel: UserModel) {
        viewModelScope.launch {
            authUseCase.updateProfile(userModel).collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> {
                        _state.value =
                            _state.value.copy(isLoading = false, isSuccess = true, error = null)
                        fetchCurrentUser()
                    }
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCase.logOut()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}