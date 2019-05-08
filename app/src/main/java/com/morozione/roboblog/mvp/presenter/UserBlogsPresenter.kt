package com.morozione.roboblog.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.view.UserBlogsView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class UserBlogsPresenter : MvpBasePresenter<UserBlogsView>() {
    private val blogDao = BlogDao()

    private var blogsIsLoading = false

    fun loadBlogsByUserId(userId: String) {
        blogDao.getBlogsByUserId(userId)
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

    fun deleteBlog(id: String) {
        compositeDisposable.add(
            blogDao.removeBlog(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.onDeleted()
                }, { e ->
                    viewState.onError()
                })
        )
    }
}