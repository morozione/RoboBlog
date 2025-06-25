package com.morozione.roboblog.ui.userblogs

import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState
import java.util.concurrent.TimeUnit

@InjectViewState
class UserBlogsPresenter : MvpBasePresenter<UserBlogsView>() {
    private val blogDao = BlogDao()

    private var blogsIsLoading = false

    fun loadBlogsByUserId(userId: String) {
        blogDao.getBlogsByUserId(userId)
            .buffer(100, TimeUnit.MILLISECONDS)
            .subscribeWithSchedulers(
                onNext = { blogs ->
                    viewState.onBlogsUploaded(blogs, blogsIsLoading)
                    blogsIsLoading = true
                },
                onError = { 
                    viewState.onError()
                    blogsIsLoading = false
                },
                onComplete = { blogsIsLoading = false }
            )
    }

    fun deleteBlog(id: String) {
        blogDao.removeBlog(id).subscribeWithSchedulers(
            onComplete = { viewState.onDeleted() },
            onError = { viewState.onError() }
        )
    }
}