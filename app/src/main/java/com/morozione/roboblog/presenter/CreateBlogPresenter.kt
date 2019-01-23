package com.morozione.roboblog.presenter

import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.presenter.view.CreateBlogView
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class CreateBlogPresenter : MvpBasePresenter<CreateBlogView>() {
    private val blogDao = BlogDao()
    var blog: Blog? = null

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