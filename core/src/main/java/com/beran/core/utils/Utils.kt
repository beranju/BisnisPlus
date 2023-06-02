package com.beran.core.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.beran.core.domain.model.BookModel
import com.google.firebase.firestore.DocumentSnapshot
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.apache.commons.csv.CSVFormat
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.Writer
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

object Utils {
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

    fun compressImage(
        filePath: String,
        maxWidth: Int = 200,
        maxHeight: Int = 200,
        quality: Int = 60
    ): ByteArray? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // ** calculate appropriate scale factor to resize iage while maintaining aspect ratio
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        val bitmap = BitmapFactory.decodeFile(filePath, options)
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedImageByteArray = outputStream.toByteArray()

        bitmap?.recycle()
        outputStream.close()

        return compressedImageByteArray
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        var cursor: Cursor? = null
        var filePath: String? = null

        try {
            cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    filePath = cursor.getString(columnIndex)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return filePath
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        maxWidth: Int,
        maxHeight: Int
    ): Int {
        var inSampleSize = 1
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight

        if (imageWidth > maxWidth || imageHeight > maxHeight) {
            val widthRatio = Math.round(imageWidth.toFloat() / maxWidth.toFloat())
            val heightRatio = Math.round(imageHeight.toFloat() / maxHeight.toFloat())
            inSampleSize = if (widthRatio < heightRatio) widthRatio else heightRatio
        }
        return inSampleSize
    }

    fun Writer.writeCsv(books: List<BookModel>) {
        CSVFormat.DEFAULT.print(this).apply {
            printRecord("Laporan Usaha")
            printRecord("Index", "Type", "Category", "Amount", "Mitra", "State", "CreatedAt")
            books.forEach { item ->
                printRecord(
                    item.bookId.toString(),
                    item.type.toString(),
                    item.category.toString(),
                    item.amount.toString(),
                    item.mitra.toString(),
                    item.state.toString(),
                    convertToDate(item.createdAt ?: 0)
                )
            }
        }
    }
}