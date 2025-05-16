package com.morozione.roboblog.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import moxy.MvpAppCompatFragment
import com.morozione.roboblog.ui.dialog.DialogFactory

abstract class BaseImageFragment : MvpAppCompatFragment() {
    companion object {
        var imageUri: Uri? = null
        const val PERMISSION_REQUEST_CODE = 2
        const val ACTION_SELECT_IMAGE = 0
        const val ACTION_IMAGE_CAPTURE = 1
    }

    protected fun makePhoto() {
        if (checkAndAskPermission()) {
            showDialogMakePhoto()
        }
    }

    private fun showDialogMakePhoto() {
        if (context == null)
            return

        DialogFactory.createDialogSelectMakingImage(requireContext()) { position ->
            when (position) {
                0 -> openGallery()
                1 -> openCamera()
            }
        }.show()
    }

    private fun openGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, ACTION_SELECT_IMAGE)
    }

    private fun openCamera() {
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "IMG")
        value.put(MediaStore.Images.Media.DESCRIPTION, "Camera")
        if (activity != null)
            imageUri = requireActivity().contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, ACTION_IMAGE_CAPTURE)
    }

    private fun checkAndAskPermission(): Boolean {
        activity?.let { activity ->
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat
                    .requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_REQUEST_CODE
                    )
                return false
            }
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat
                    .requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                return false
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || context == null)
            return

        if (requestCode == ACTION_SELECT_IMAGE) {
            imageUri = data!!.data
        }
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            requireContext().contentResolver.query(imageUri!!, filePathColumn, null, null, null)
        val columnIndex: Int
        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(filePathColumn[0])
            cursor.moveToFirst()
            imageUri = Uri.parse(cursor.getString(columnIndex))
            cursor.close()
        }

        imageMade(imageUri.toString())
    }

    abstract fun imageMade(imageUri: String?)
}