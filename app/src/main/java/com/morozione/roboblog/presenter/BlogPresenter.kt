package com.morozione.roboblog.presenter

import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.presenter.view.BlogView
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

@InjectViewState
class BlogPresenter : MvpBasePresenter<BlogView>() {
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