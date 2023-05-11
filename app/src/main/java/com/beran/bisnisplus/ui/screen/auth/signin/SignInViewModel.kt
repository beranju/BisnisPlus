package com.beran.bisnisplus.ui.screen.auth.signin

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel(private val useCase: AuthUseCase) : ViewModel() {
    private var _uiState: MutableStateFlow<SignInState<Unit>> =
        MutableStateFlow(SignInState.Initial)
    val uiState: StateFlow<SignInState<Unit>> get() = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            useCase.signIn(email, password).collect { result ->
                when (result) {
                    is Resource.Success -> _uiState.value = SignInState.Success(result.data)
                    is Resource.Error -> _uiState.value = SignInState.Error(result.message)
                    is Resource.Loading -> _uiState.value = SignInState.Loading
                }
            }
        }
    }

    fun oneTapSignIn(intent: Intent) {
        viewModelScope.launch {
            useCase.oneTapSignIn(intent).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = SignInState.Loading
                    is Resource.Error -> _uiState.value = SignInState.Error(result.message)
                    is Resource.Success -> _uiState.value = SignInState.Success(Unit)
                }

            }
        }
    }

    suspend fun getSignInIntent(): IntentSender? = useCase.getSignInIntent()

}