package com.morozione.roboblog.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException

object ImageUtil {
    fun decodeSampledBitmapFromResource(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            // Check if the file was successfully read for dimensions
            if (options.outWidth == -1 || options.outHeight == -1) {
                return null
            }

            options.inSampleSize = calculateInSampleSize(
                options, reqWidth,
                reqHeight
            )

            options.inJustDecodeBounds = false
            BitmapFactory.decodeFile(path, options)
        } catch (e: Exception) {
            null
        }
    }
    
    fun decodeSampledBitmapFromUri(context: Context, uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        return try {
            val contentResolver = context.contentResolver
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            // Check if the file was successfully read for dimensions
            if (options.outWidth == -1 || options.outHeight == -1) {
                return null
            }

            options.inSampleSize = calculateInSampleSize(
                options, reqWidth,
                reqHeight
            )

            options.inJustDecodeBounds = false
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
