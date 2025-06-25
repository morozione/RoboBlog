package com.morozione.roboblog.ui.globaldetails

import moxy.InjectViewState
import com.morozione.roboblog.Constants
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class GlobalDetailsPresenter : MvpBasePresenter<GlobalDetailsView>() {
    private val userDao = UserDao()
    private val blogDao = BlogDao()

    fun onSetRating(blog: Blog, rating: Int) {
        userDao.changeValue(blog, rating, Constants.BLOG_RATING)

        compositeDisposable.add(
            blogDao.appreciateBlog(blog, UserDao.getCurrentUserId(), rating)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState.onRatingSuccess() },
                    { viewState.onError() }
                )
        )
    }
}