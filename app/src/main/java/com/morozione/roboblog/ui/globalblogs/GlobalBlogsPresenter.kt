package com.morozione.roboblog.ui.globalblogs

import moxy.InjectViewState
import com.morozione.roboblog.Constants
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class GlobalBlogsPresenter : MvpBasePresenter<GlobalBlogsView>() {

    private val blogDao = BlogDao()
    private val userDao = UserDao()

    private var blogsIsLoading = false

    fun loadBlogs() {
        compositeDisposable.add(
            blogDao.getBlogs()
                .buffer(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { blogs ->
                        viewState.onBlogsUploaded(blogs, blogsIsLoading)
                        blogsIsLoading = true
                    },
                    { 
                        viewState.onError()
                        blogsIsLoading = false
                    },
                    { blogsIsLoading = false }
                )
        )
    }

    fun setRating(blog: Blog, rating: Int) {
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