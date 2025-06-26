package com.morozione.roboblog.ui.createblog

import android.app.Activity
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.ImageUploadUtils
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState

@InjectViewState
class CreateBlogPresenter : MvpBasePresenter<CreateBlogView>() {
    private val blogDao = BlogDao()
    var blog: Blog? = null

    fun createBlog(blog: Blog, imageUri: String?, activity: Activity) {
        if (imageUri != null && imageUri.isNotEmpty()) {
            val imageUploadUtils = ImageUploadUtils(activity, compositeDisposable)
            val patchs = ArrayList<String>()
            patchs.add(imageUri.toString())
            imageUploadUtils.uploadImages(
                patchs, 
                blog.id,
                onResult = { patchs1 ->
                    blog.icon = patchs1[0]
                    createBlog(blog)
                },
                onError = { viewState.onError() }
            )
        } else {
            createBlog(blog)
        }
    }

    fun createBlog(blog: Blog) {
        blogDao.create(blog).subscribeWithSchedulers(
            onComplete = { viewState.onBlogCreated() },
            onError = { viewState.onError() }
        )
    }

    fun updateBlog(blog: Blog) {
        blogDao.update(blog).subscribeWithSchedulers(
            onComplete = { viewState.onBlogUpdated() },
            onError = { viewState.onError() }
        )
    }
}