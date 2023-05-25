package com.beran.bisnisplus.ui.screen.auth.dataBisnis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.model.BusinessModel
import com.beran.core.domain.model.UserModel
import com.beran.core.domain.usecase.AuthUseCase
import com.beran.core.domain.usecase.bisnis.BisnisUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BisnisViewModel(private val useCase: BisnisUseCase, private val authUseCase: AuthUseCase) : ViewModel() {
    private var _uiState: MutableStateFlow<BisnisState> = MutableStateFlow(BisnisState.Initial)
    val uiState get() = _uiState.asStateFlow()

    val currentUser: UserModel? = authUseCase.currentUser()

    fun createNewBisnis(businessModel: BusinessModel
    ) {
        viewModelScope.launch {
            useCase.createNewBisnis(businessModel)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _uiState.value = BisnisState.Loading
                        is Resource.Success -> _uiState.value = BisnisState.Success(Unit)
                        is Resource.Error -> _uiState.value = BisnisState.Error(result.message)
                    }
                }
        }
    }
}