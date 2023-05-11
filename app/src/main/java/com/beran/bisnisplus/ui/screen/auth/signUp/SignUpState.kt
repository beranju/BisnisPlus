package com.beran.bisnisplus.ui.screen.auth.signUp

sealed class SignUpState<out R> private constructor(){
    data class Success<T>(val data: T): SignUpState<T>()
    data class Error(val message: String): SignUpState<Nothing>()
    object Loading: SignUpState<Nothing>()
    object Initial: SignUpState<Nothing>()
}