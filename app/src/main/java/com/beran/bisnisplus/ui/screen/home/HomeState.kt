package com.beran.bisnisplus.ui.screen.home.common

import com.beran.core.domain.model.BookModel
import com.beran.core.domain.model.UserModel
import com.github.mikephil.charting.data.PieEntry


data class HomeStates(
    val listBook: List<BookModel> = emptyList(),
    val pieEntry: List<PieEntry> = emptyList(),
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
