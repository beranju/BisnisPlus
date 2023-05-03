package com.beran.bisnisplus.utils

import java.text.NumberFormat
import java.util.Locale

object Utils {

    fun rupiahFormatter(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount.toLong())
    }
}