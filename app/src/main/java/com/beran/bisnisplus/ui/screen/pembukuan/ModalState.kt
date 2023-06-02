package com.beran.bisnisplus.ui.screen.pembukuan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ModalState(initialValue: Boolean = false) {
    var isVisible by mutableStateOf(initialValue)
    fun open(){
        isVisible = true
    }
    fun close(){
        isVisible = false
    }
}