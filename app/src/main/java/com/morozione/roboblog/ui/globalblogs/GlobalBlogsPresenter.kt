package com.morozione.roboblog.ui.globalblogs

import com.morozione.roboblog.Constants
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState
import java.util.concurrent.TimeUnit

@InjectViewState
class GlobalBlogsPresenter : MvpBasePresenter<GlobalBlogsView>() {

    private val blogDao = BlogDao()
    private val userDao = UserDao()

    private var blogsIsLoading = false

    fun loadBlogs() {
        blogDao.getBlogs()
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

    fun setRating(blog: Blog, rating: Int) {
        userDao.changeValue(blog, rating, Constants.BLOG_RATING)

        blogDao.appreciateBlog(blog, UserDao.getCurrentUserId(), rating).subscribeWithSchedulers(
            onComplete = { viewState.onRatingSuccess() },
            onError = { viewState.onError() }
        )
    }
}