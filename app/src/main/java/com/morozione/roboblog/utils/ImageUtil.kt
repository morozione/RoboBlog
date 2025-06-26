package com.morozione.roboblog.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.IOException

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
            val bitmap = BitmapFactory.decodeFile(path, options)
            
            // Handle EXIF orientation
            bitmap?.let { rotateImageIfRequired(it, path) }
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
            val bitmap = contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }
            
            // Handle EXIF orientation for URI
            bitmap?.let { rotateImageIfRequired(context, it, uri) }
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
    
    /**
     * Rotate image if required based on EXIF orientation data
     */
    private fun rotateImageIfRequired(bitmap: Bitmap, imagePath: String): Bitmap {
        return try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: IOException) {
            bitmap // Return original bitmap if EXIF reading fails
        }
    }
    
    /**
     * Rotate image if required for URI (content://) based on EXIF orientation data
     */
    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val exif = ExifInterface(inputStream)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                    else -> bitmap
                }
            } ?: bitmap
        } catch (e: IOException) {
            bitmap // Return original bitmap if EXIF reading fails
        }
    }
    
    /**
     * Rotate bitmap by specified degrees
     */
    private fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        
        return try {
            val rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )
            
            // Recycle original bitmap if it's different from the rotated one
            if (rotatedBitmap != bitmap) {
                bitmap.recycle()
            }
            
            rotatedBitmap
        } catch (e: OutOfMemoryError) {
            bitmap // Return original bitmap if rotation fails due to memory issues
        }
    }
}
