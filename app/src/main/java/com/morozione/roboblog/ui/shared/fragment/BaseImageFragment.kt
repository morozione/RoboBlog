package com.morozione.roboblog.ui.shared.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import moxy.MvpAppCompatFragment
import com.morozione.roboblog.ui.shared.dialog.DialogFactory

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
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, ACTION_SELECT_IMAGE)
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
            val permissionsToRequest = mutableListOf<String>()
            
            // Camera permission is always needed
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.CAMERA)
            }
            
            // Media permissions based on Android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+) - Use granular media permissions
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                // Android 12 and below - Use READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            
            // Request permissions if any are missing
            if (permissionsToRequest.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    permissionsToRequest.toTypedArray(),
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

        when (requestCode) {
            ACTION_SELECT_IMAGE -> {
                // For gallery selection, use the URI directly
                imageUri = data?.data
                if (imageUri != null) {
                    imageMade(imageUri.toString())
                }
            }
            ACTION_IMAGE_CAPTURE -> {
                // For camera capture, the imageUri is already set in openCamera()
                // Don't reassign it from data?.data as it's usually null
                if (imageUri != null) {
                    imageMade(imageUri.toString())
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted, show the photo dialog
                showDialogMakePhoto()
            } else {
                // Some permissions denied, you might want to show a message to the user
                // For now, we'll just not show the dialog
            }
        }
    }

    abstract fun imageMade(imageUri: String?)
}