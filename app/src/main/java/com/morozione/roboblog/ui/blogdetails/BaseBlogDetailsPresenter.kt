package com.morozione.roboblog.ui.blogdetails

import moxy.InjectViewState
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class BaseBlogDetailsPresenter : MvpBasePresenter<BaseBlogDetailsView>() {
    private val blogDao = BlogDao()

    fun loadBlog(id: String) {
        compositeDisposable.add(
            blogDao.getBlogsById(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { blog -> viewState.onBlogUploaded(blog) },
                    { viewState.onError() }
                )
        )
    }
}