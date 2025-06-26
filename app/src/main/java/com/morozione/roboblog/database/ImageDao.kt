package com.morozione.roboblog.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.storage
import io.reactivex.rxjava3.core.Single
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class ImageDao : BaseDao() {
    private var storageRef = Firebase.storage.reference

    fun load(name: String): Single<Bitmap> {
        return Single.create { emitter ->
            try {
                val localFile = File.createTempFile("Images", "jpg")
                storageRef.child(name)
                    .getFile(localFile)
                    .addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        if (bitmap != null) {
                            emitter.onSuccess(bitmap)
                        } else {
                            emitter.onError(Exception("Failed to decode bitmap"))
                        }
                        // Clean up temp file
                        localFile.delete()
                    }
                    .addOnFailureListener { emitter.onError(it) }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    fun save(uri: String): Single<String> {
        return Single.create { emitter ->
            try {
                val metadata = StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build()

                val file = Uri.fromFile(File(uri))
                val fileName = "image-${file.lastPathSegment?.hashCode()}-${file.lastPathSegment}"
                
                storageRef.child(fileName)
                    .putFile(file, metadata)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata?.reference?.downloadUrl
                            ?.addOnSuccessListener { downloadUrl ->
                                emitter.onSuccess(downloadUrl.toString())
                            }
                            ?.addOnFailureListener { emitter.onError(it) }
                            ?: emitter.onError(Exception("Failed to get download URL"))
                    }
                    .addOnFailureListener { emitter.onError(it) }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    /**
     * Save bitmap to Firebase Storage
     * Note: EXIF rotation should be handled by ImageUtil before calling this method
     */
    fun saveBitmap(bitmap: Bitmap, nameImage: String): Single<String> {
        return Single.create { emitter ->
            try {
                val path = "image-$nameImage"
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos) // Reduced quality for smaller size
                val data = baos.toByteArray()
                baos.close()

                storageRef.child(path)
                    .putBytes(data)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata?.reference?.downloadUrl
                            ?.addOnSuccessListener { downloadUrl ->
                                emitter.onSuccess(downloadUrl.toString())
                            }
                            ?.addOnFailureListener { emitter.onError(it) }
                            ?: emitter.onError(Exception("Failed to get download URL"))
                    }
                    .addOnFailureListener { emitter.onError(it) }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
    
    /**
     * Save bitmap with proper orientation handling - alternative method 
     * This method can be used if ImageUtil rotation doesn't work properly
     */
    fun saveBitmapWithRotation(bitmap: Bitmap, nameImage: String, originalPath: String? = null): Single<String> {
        return Single.create { emitter ->
            try {
                val path = "image-$nameImage"
                
                // Apply rotation if needed based on original path
                val correctedBitmap = originalPath?.let { path ->
                    try {
                        val exif = ExifInterface(path)
                        val orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL
                        )
                        
                        when (orientation) {
                            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                            else -> bitmap
                        }
                    } catch (e: IOException) {
                        bitmap // Return original bitmap if EXIF reading fails
                    }
                } ?: bitmap
                
                val baos = ByteArrayOutputStream()
                correctedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                val data = baos.toByteArray()
                baos.close()

                storageRef.child(path)
                    .putBytes(data)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata?.reference?.downloadUrl
                            ?.addOnSuccessListener { downloadUrl ->
                                emitter.onSuccess(downloadUrl.toString())
                            }
                            ?.addOnFailureListener { emitter.onError(it) }
                            ?: emitter.onError(Exception("Failed to get download URL"))
                    }
                    .addOnFailureListener { emitter.onError(it) }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
    
    /**
     * Rotate bitmap by specified degrees
     */
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        
        return try {
            Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )
        } catch (e: OutOfMemoryError) {
            bitmap // Return original bitmap if rotation fails due to memory issues
        }
    }
}