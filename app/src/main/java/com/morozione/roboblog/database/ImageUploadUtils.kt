package com.morozione.roboblog.database

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import com.morozione.roboblog.R
import com.morozione.roboblog.utils.ImageUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

class ImageUploadUtils(activity: Activity, private val compositeDisposable: CompositeDisposable) {

    private lateinit var paths: MutableList<String>
    private val activity: WeakReference<Activity> = WeakReference(activity)
    private var progressDialog: ProgressDialog? = null
    private var prefix: String? = null
    private var countOfLoadedImages: Int = 0
    private lateinit var linksResult: (MutableList<String>) -> Unit

    private var imageDao = ImageDao()

    fun uploadImages(
        paths: MutableList<String>,
        prefix: String,
        linksResult: (MutableList<String>) -> Unit
    ) {
        this.paths = paths
        this.prefix = prefix
        this.linksResult = linksResult
        imageDao = ImageDao()
        countOfLoadedImages = 0

        if (!paths.isEmpty()) {
            showProgressDialog()
            checkPath()
        }
    }

    private fun checkPath() {
        val px = 300
        for (i in paths.indices) {
            val s = paths[i].substring(0, 6)
            if (!paths[i].contains("https://")) {

                if (progressDialog!!.isShowing)
                    progressDialog!!.setMessage("upload " + (i + 1) + "/" + paths.size)

                val uri = Uri.parse(paths[i])
                val bitmap = if (paths[i].startsWith("content://")) {
                    activity.get()?.let { context ->
                        ImageUtil.decodeSampledBitmapFromUri(context, uri, px, px)
                    }
                } else {
                    ImageUtil.decodeSampledBitmapFromResource(paths[i], px, px)
                }
                
                if (bitmap == null) {
                    progressDialog?.dismiss()
                    return
                }

                compositeDisposable.add(
                    imageDao.saveBitmap(
                        bitmap,
                        prefix + "-" + uri.lastPathSegment
                    )
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ result ->
                            for (i in paths.indices) {
                                if (paths[i].contains("https://"))
                                    continue

                                countOfLoadedImages++
                                paths[i] = result
                                checkPath()
                                break
                            }
                            if (countOfLoadedImages == paths.size)
                                linksResult(paths)
                        }, { e ->
                            progressDialog!!.dismiss()
                        })
                )
                break
            }
            if (i == paths.size - 1) {
                if (progressDialog!!.isShowing)
                    progressDialog!!.dismiss()
            }
        }
    }

    private fun showProgressDialog() {
        activity.get()?.let { activity ->
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setTitle(activity.getString(R.string.image_download_progress_title))
            progressDialog!!.setMessage(activity.getString(R.string.image_download_progress_text))
            progressDialog!!.show()
        }
    }
}