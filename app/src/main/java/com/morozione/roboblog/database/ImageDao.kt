package com.morozione.roboblog.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import io.reactivex.rxjava3.core.Single
import java.io.ByteArrayOutputStream
import java.io.File

class ImageDao {
    private var storageRef: StorageReference? = null

    init {
        if (storageRef == null) {
            storageRef = FirebaseStorage.getInstance().reference
        }
    }

    fun load(name: String) = Single.create<Bitmap> { emmiter ->
        val localFile = File.createTempFile("Images", "jpg")

        localFile?.let {
            storageRef?.child(name)?.getFile(it)?.addOnSuccessListener { taskSnapshot ->
                emmiter.onSuccess(BitmapFactory.decodeFile(localFile.absolutePath))
            }?.addOnFailureListener { e -> emmiter.onError(e) }
        } ?: emmiter.onError(Error())
    }

    fun save(uri: String) = Single.create<String> { emmiter ->
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpg")
            .build()

        val file = Uri.fromFile(File(uri))
        val s = "image-" + file.lastPathSegment!!.hashCode() + "-" + file.lastPathSegment
        val uploadTask = storageRef?.child(s)?.putFile(file, metadata)

        uploadTask?.addOnSuccessListener {
            emmiter.onSuccess(s)
        }?.addOnFailureListener { e -> emmiter.onError(e) }
    }

    fun saveBitmap(bitmap: Bitmap, nameImage: String) = Single.create<String> { emmiter ->
        val path = "image-$nameImage"
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = storageRef?.child(path)?.putBytes(data)
        uploadTask?.addOnFailureListener { e -> }?.addOnSuccessListener { taskSnapshot ->
            taskSnapshot?.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                emmiter.onSuccess(it.toString())
            }
        }
    }
}