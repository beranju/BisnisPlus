package com.beran.bisnisplus.ui.screen.home.common

sealed class HomeState<out R> private constructor() {
    data class Success<T>(val data: T) : HomeState<T>()
    data class Error(val message: String) : HomeState<Nothing>()
    object Loading : HomeState<Nothing>()
}
