package com.beran.bisnisplus.ui.screen.auth.signUp

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SignUpViewModel(private val useCase: AuthUseCase) : ViewModel() {
    private var _uiState: MutableStateFlow<SignUpState<Unit>> =
        MutableStateFlow(SignUpState.Initial)
    val uiState: StateFlow<SignUpState<Unit>> get() = _uiState

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            useCase.signUp(name, email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = SignUpState.Loading
                    is Resource.Error -> _uiState.value = SignUpState.Error(result.message)
                    is Resource.Success -> _uiState.value = SignUpState.Success(result.data)
                }
            }
        }
    }

    fun oneTapSignUp(intent: Intent){
        viewModelScope.launch {
            useCase.oneTapSignUp(intent).collect{result ->
                when(result){
                    is Resource.Loading -> _uiState.value = SignUpState.Loading
                    is Resource.Error -> _uiState.value = SignUpState.Error(result.message)
                    is Resource.Success -> {
                        Timber.tag("SignViewModel").i("data: ${result.data}")
                        _uiState.value = SignUpState.Success(Unit)
                    }
                }

            }
        }
    }

    suspend fun getSignUpIntent(): IntentSender? = useCase.getSignUpIntent()
}