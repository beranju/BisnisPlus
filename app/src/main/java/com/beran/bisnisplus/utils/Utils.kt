package com.beran.bisnisplus.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.ContactsContract
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.beran.core.domain.model.Contact
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import java.util.UUID

object Utils {

    fun rupiahFormatter(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount)
    }

    fun convertToDate(epoch: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instant = Instant.ofEpochMilli(epoch)
            val localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val dayOfWeekName =
                localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val monthName = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val dayOfMonth = localDate.dayOfMonth
            val year = localDate.year
            "$dayOfWeekName, $dayOfMonth $monthName $year"
        } else {
            val date = Date(epoch)
            val dateFormat = SimpleDateFormat("EEEE,d MMMM yyyy", Locale.getDefault())
            dateFormat.format(date)
        }
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

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        val contentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        if (cursor == null){
            filePath = uri.path
        }else{
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.DownloadColumns.DISPLAY_NAME)
        }

        cursor?.close()
        return filePath

    }

    fun getFileFromUri(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val parcelFileDescriptor: ParcelFileDescriptor? =
            contentResolver.openFileDescriptor(uri, "r")
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