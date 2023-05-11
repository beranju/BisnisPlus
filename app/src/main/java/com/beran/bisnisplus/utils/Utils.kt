package com.beran.bisnisplus.utils

import java.text.NumberFormat
import java.util.Locale

object Utils {

    fun rupiahFormatter(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount.toLong())
    }

    /**
     * this utils used to validate email input...
     * ...use the regular expression pattern...
     * ...that matches the valid email
     */
    fun isValidEmail(input: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(input)
    }
}