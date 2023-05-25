package com.beran.bisnisplus.ui.screen.pembukuan

sealed class BookState<out R> private constructor() {
    data class Success<T>(val data: T) : BookState<T>()
    data class Error(val message: String) : BookState<Nothing>()
    object Loading : BookState<Nothing>()
    object Initial : BookState<Nothing>()
}

sealed class BookStates<out R> private constructor() {
    data class Success<T>(val data: T) : BookStates<T>()
    data class Error(val message: String) : BookStates<Nothing>()
    object Loading : BookStates<Nothing>()
}
