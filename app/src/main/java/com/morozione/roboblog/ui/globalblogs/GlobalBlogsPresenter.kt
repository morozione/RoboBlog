package com.morozione.roboblog.ui.globalblogs

import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState

@InjectViewState
class GlobalBlogsPresenter : MvpBasePresenter<GlobalBlogsView>() {

    private val blogDao = BlogDao()
    private val userDao = UserDao()

    private var blogsIsLoading = false

    fun loadBlogs() {
        blogDao.getBlogs()
            .subscribeWithSchedulers(
                onNext = { blogs ->
                    viewState.onBlogsUploaded(blogs, blogsIsLoading)
                    blogsIsLoading = true
                },
                onError = { 
                    viewState.onError()
                    blogsIsLoading = false
                }
            )
    }

    fun setRating(blog: Blog, rating: Int) {
        blogDao.appreciateBlog(blog, UserDao.getCurrentUserId(), rating).subscribeWithSchedulers(
            onComplete = { viewState.onRatingSuccess() },
            onError = { viewState.onError() }
        )
    }
}