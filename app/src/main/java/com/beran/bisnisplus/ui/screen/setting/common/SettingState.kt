package com.beran.bisnisplus.ui.screen.setting.common

sealed class SettingState<out R>private constructor(){
    data class Success<T>(val data: T): SettingState<T>()
    data class Error<Nothing>(val message:String): SettingState<Nothing>()
    object Loading: SettingState<Nothing>()
}
sealed class SettingStates<out R>private constructor(){
    data class Success<T>(val data: T): SettingStates<T>()
    data class Error<Nothing>(val message:String): SettingStates<Nothing>()
    object Loading: SettingStates<Nothing>()
    object Initial: SettingStates<Nothing>()
}
