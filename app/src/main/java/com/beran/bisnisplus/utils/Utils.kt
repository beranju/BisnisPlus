package com.beran.bisnisplus.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import com.beran.core.domain.model.Contact
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

object Utils {

    fun rupiahFormatter(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount)
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

    fun getFileFromUri(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val parcelFileDescriptor : ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor

        val file = File(context.cacheDir, "temp.file")
        val inputStream = FileInputStream(fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        parcelFileDescriptor?.close()
        inputStream.close()
        outputStream.close()
        return file.path
    }

    fun generateUUid(): String {
        return UUID.randomUUID().toString()
    }

}