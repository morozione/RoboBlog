package com.morozione.roboblog.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.Constants
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.view.GlobalBlogsView
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class GlobalBlogsPresenter : MvpBasePresenter<GlobalBlogsView>() {

    private val blogDao = BlogDao()
    private val userDao = UserDao()

    private var blogsIsLoading = false

    fun loadBlogs() {
        blogDao.getBlogs()
            .buffer(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Blog>> {
                override fun onComplete() {
                    blogsIsLoading = false
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(blogs: List<Blog>) {
                    viewState.onBlogsUploaded(blogs, blogsIsLoading)

                    blogsIsLoading = true
                }

                override fun onError(e: Throwable) {
                    viewState.onError()

                    blogsIsLoading = false
                }

            })
    }

    fun setRating(blog: Blog, rating: Int) {
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