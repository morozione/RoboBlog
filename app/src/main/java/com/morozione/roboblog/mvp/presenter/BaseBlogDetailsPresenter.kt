package com.morozione.roboblog.mvp.presenter

import moxy.InjectViewState
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.view.BaseBlogDetailsView
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class BaseBlogDetailsPresenter : MvpBasePresenter<BaseBlogDetailsView>() {
    private val blogDao = BlogDao()

    fun loadBlog(id: String) {
        blogDao.getBlogsById(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Blog> {
                override fun onSuccess(t: Blog) {
                    viewState.onBlogUploaded(t)
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