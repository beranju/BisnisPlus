package com.beran.bisnisplus.ui.screen.auth.dataBisnis

sealed interface BisnisState {
    data class Success(val data: Unit): BisnisState
    data class Error(val message: String): BisnisState
    object Loading: BisnisState
    object Initial: BisnisState
}