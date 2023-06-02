package com.beran.bisnisplus.ui.screen.setting.common

import com.beran.core.domain.model.UserModel


data class SettingState(
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
