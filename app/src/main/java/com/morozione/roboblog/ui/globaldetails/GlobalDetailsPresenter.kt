package com.morozione.roboblog.ui.globaldetails

import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState


@InjectViewState
class GlobalDetailsPresenter : MvpBasePresenter<GlobalDetailsView>() {
    private val userDao = UserDao()
    private val blogDao = BlogDao()

    fun onSetRating(blog: Blog, rating: Int) {
        blogDao.appreciateBlog(blog, UserDao.getCurrentUserId(), rating).subscribeWithSchedulers(
            onComplete = { viewState.onRatingSuccess() },
            onError = { viewState.onError() }
        )
    }
}