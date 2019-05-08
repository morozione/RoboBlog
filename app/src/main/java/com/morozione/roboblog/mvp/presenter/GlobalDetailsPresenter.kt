package com.morozione.roboblog.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.Constants
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.view.GlobalDetailsView
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class GlobalDetailsPresenter : MvpBasePresenter<GlobalDetailsView>() {
    private val userDao = UserDao()
    private val blogDao = BlogDao()

    fun onSetRating(blog: Blog, rating: Int) {
        userDao.changeValue(blog, rating, Constants.BLOG_RATING)

        blogDao.appreciateBlog(blog, UserDao.getCurrentUserId(), rating)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    viewState.onRatingSuccess()
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