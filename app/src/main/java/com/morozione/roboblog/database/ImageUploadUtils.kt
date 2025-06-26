package com.morozione.roboblog.database

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import com.morozione.roboblog.R
import com.morozione.roboblog.utils.ImageUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

class ImageUploadUtils(activity: Activity, private val compositeDisposable: CompositeDisposable) {

    private val activityRef: WeakReference<Activity> = WeakReference(activity)
    private var progressDialog: ProgressDialog? = null
    private val imageDao = ImageDao()

    fun uploadImages(
        paths: MutableList<String>,
        prefix: String,
        onResult: (MutableList<String>) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        if (paths.isEmpty()) {
            onResult(mutableListOf())
            return
        }

        showProgressDialog()
        
        compositeDisposable.add(
            Observable.fromIterable(paths.withIndex())
                .flatMap { (index, path) ->
                    if (path.startsWith("https://")) {
                        // Already uploaded, return as is
                        Observable.just(path)
                    } else {
                        // Upload new image
                        uploadSingleImage(path, prefix, index)
                            .toObservable()
                            .doOnNext { updateProgress(index + 1, paths.size) }
                    }
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { results ->
                        hideProgressDialog()
                        onResult(results.toMutableList())
                    },
                    { error ->
                        hideProgressDialog()
                        onError(error)
                    }
                )
        )
    }

    private fun uploadSingleImage(path: String, prefix: String, index: Int) = 
        imageDao.saveBitmap(
            // createBitmapFromPath now automatically handles EXIF rotation
            createBitmapFromPath(path) ?: throw Exception("Failed to create bitmap from path: $path"),
            "$prefix-${Uri.parse(path).lastPathSegment ?: "image_$index"}"
        )

    /**
     * Create bitmap from path with automatic EXIF rotation handling
     * This ensures images are correctly oriented regardless of camera capture orientation
     */
    private fun createBitmapFromPath(path: String, targetSize: Int = 300) = 
        activityRef.get()?.let { activity ->
            if (path.startsWith("content://")) {
                ImageUtil.decodeSampledBitmapFromUri(activity, Uri.parse(path), targetSize, targetSize)
            } else {
                ImageUtil.decodeSampledBitmapFromResource(path, targetSize, targetSize)
            }
        }

    private fun updateProgress(current: Int, total: Int) {
        progressDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.setMessage("Uploading $current/$total")
            }
        }
    }

    private fun showProgressDialog() {
        activityRef.get()?.let { activity ->
            progressDialog = ProgressDialog(activity).apply {
                setProgressStyle(ProgressDialog.STYLE_SPINNER)
                setCancelable(false)
                setTitle(activity.getString(R.string.image_download_progress_title))
                setMessage(activity.getString(R.string.image_download_progress_text))
                show()
            }
        }
    }

    private fun hideProgressDialog() {
        progressDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        progressDialog = null
    }

    fun dispose() {
        hideProgressDialog()
    }
}