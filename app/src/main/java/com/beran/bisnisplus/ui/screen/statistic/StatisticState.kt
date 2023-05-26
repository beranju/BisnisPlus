package com.beran.bisnisplus.ui.screen.statistic

import com.github.mikephil.charting.data.Entry

data class StatisticState(
    val listIncomeDate: List<Entry> = emptyList(),
    val listExpenseData: List<Entry> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val incomeStartDate: Long? = null,
    val expenseStartDate: Long? = null,
    val endData: Long? = null,
)
