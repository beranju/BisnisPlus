package com.beran.bisnisplus.utils

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import com.beran.core.domain.model.Contact
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object Utils {

    fun rupiahFormatter(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount.toLong())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDate.toEpochMilli(): Long {
        val zoneId = ZoneId.systemDefault()
        val zoneLocalDate = this.atStartOfDay(zoneId)
        val instantDate = zoneLocalDate.toInstant()
        return instantDate.toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDate(epoch: Long): String{
        val instant = Instant.ofEpochMilli(epoch)
        val localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val dayOfWeekName = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val monthName = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = localDate.dayOfMonth
        val year = localDate.year
        return "$dayOfWeekName, $dayOfMonth $monthName $year"
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

    fun getContacts(contactUri: Uri?, contentResolver: ContentResolver): Contact? {
        val contactCursor = contactUri?.let { contentResolver.query(it, null, null, null, null) }
        contactCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val contactName: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val contactPhone: String? =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                return Contact(contactName, contactPhone?.toInt())
            }
        }

        return null
    }
}