package com.beran.bisnisplus.ui.screen.auth.signin

sealed class SignInState<out R> private constructor(){
    data class Success<T>(val data: T): SignInState<T>()
    data class Error(val message: String): SignInState<Nothing>()
    object Loading: SignInState<Nothing>()
    object Initial: SignInState<Nothing>()
}