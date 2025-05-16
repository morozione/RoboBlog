package com.morozione.roboblog.mvp.presenter

import android.app.Activity
import android.net.Uri
import moxy.InjectViewState
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.ImageUploadUtils
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.view.CreateBlogView
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList

@InjectViewState
class CreateBlogPresenter : MvpBasePresenter<CreateBlogView>() {
    private val blogDao = BlogDao()
    var blog: Blog? = null

    fun createBlog(blog: Blog, imageUri: String?, activity: Activity) {
        if (imageUri != null && imageUri.isNotEmpty()) {
            val imageUploadUtils = ImageUploadUtils(activity, compositeDisposable)
            val patchs = ArrayList<String>()
            patchs.add(imageUri.toString())
            imageUploadUtils.uploadImages(patchs, blog.id) { patchs1 ->
                blog.icon = patchs1[0]
                createBlog(blog)
            }
        } else
            createBlog(blog)
    }

    fun createBlog(blog: Blog) {
        blogDao.create(blog)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    viewState.onBlogCreated()
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    viewState.onError()
                }
            })
    }

    fun updateBlog(blog: Blog) {
        blogDao.update(blog)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    viewState.onBlogUpdated()
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    viewState.onError()
                }
            })
    }
}