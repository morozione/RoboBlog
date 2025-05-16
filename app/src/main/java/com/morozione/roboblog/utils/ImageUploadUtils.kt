//package com.morozione.roboblog.utils
//
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.app.ProgressDialog
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Handler
//import android.os.Message
//
//import com.maxml.offirent.R
//import com.maxml.offirent.dao.ImageDao
//import com.maxml.offirent.listener.OffirentCallback
//
//class ImageUploadUtils(private val activity: Activity) {
//
//    private var paths: MutableList<String>? = null
//    private var progressDialog: ProgressDialog? = null
//    private var prefix: String? = null
//    private var callback: OffirentCallback<List<String>>? = null
//    private var countOfLoatedImages: Int = 0
//
//    private var imageDao: ImageDao? = null
//
//    /**
//     * @param paths  links to images
//     * @param prefix You need use Id home of user
//     */
//
//    fun uploadImages(paths: MutableList<String>?, prefix: String, callback: OffirentCallback<List<String>>) {
//        val handler = createHandler()
//        this.paths = paths
//        this.prefix = prefix
//        imageDao = ImageDao(handler)
//        this.callback = callback
//        countOfLoatedImages = 0
//
//        if (paths != null && !paths.isEmpty()) {
//            showProgressDialog()
//            checkPath()
//        }
//    }
//
//    private fun checkPath() {
//        val px = 300
//        for (i in paths!!.indices) {
//            val s = paths!![i].substring(0, 6)
//            if (!paths!![i].contains("https://")) {
//
//                if (progressDialog!!.isShowing)
//                    progressDialog!!.setMessage("upload " + (i + 1) + "/" + paths!!.size)
//
//                val uri = Uri.parse(paths!![i])
//                val bitmap = ImageUtil.decodeSampledBitmapFromResource(paths!![i], px, px)
//                imageDao!!.saveBitmap(bitmap, prefix + "-" + uri.lastPathSegment)
//                break
//            }
//            if (i == paths!!.size - 1) {
//                if (progressDialog!!.isShowing)
//                    progressDialog!!.dismiss()
//            }
//        }
//    }
//
//    private fun showProgressDialog() {
//        progressDialog = ProgressDialog(activity)
//        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//        progressDialog!!.setCancelable(false)
//        progressDialog!!.setTitle(activity.getString(R.string.image_download_progress_title))
//        progressDialog!!.setMessage(activity.getString(R.string.image_download_progress_text))
//        progressDialog!!.show()
//    }
//
//    @SuppressLint("HandlerLeak")
//    private fun createHandler(): Handler {
//        return object : Handler() {
//            override fun handleMessage(msg: Message) {
//                super.handleMessage(msg)
//                when (msg.what) {
//                    OffirentConstants.HOMEDAO_RESULT_ERR -> if (progressDialog!!.isShowing)
//                        progressDialog!!.dismiss()
//                    OffirentConstants.IMAGEDAO_UPLOAD_PROGRESS -> {
//                        val percent = msg.obj as Double
//                        if (progressDialog!!.isShowing)
//                            progressDialog!!.progress = percent.toInt()
//                    }
//                    OffirentConstants.IMAGEDAO_RESULT_PATH_OK -> {
//                        val path = msg.obj as String
//                        for (i in paths!!.indices) {
//                            if (paths!![i].contains("https://"))
//                                continue
//
//                            countOfLoatedImages++
//                            paths!![i] = path
//                            checkPath()
//                            break
//                        }
//                        if (countOfLoatedImages == paths!!.size)
//                            callback!!.onCallback(paths)
//                    }
//                }
//            }
//        }
//    }
//}