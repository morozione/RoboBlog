package com.morozione.roboblog.ui.blogdetails

import com.morozione.roboblog.database.BlogDao
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState


@InjectViewState
class BaseBlogDetailsPresenter : MvpBasePresenter<BaseBlogDetailsView>() {
    private val blogDao = BlogDao()

    fun loadBlog(id: String) {
        blogDao.getBlogsById(id).subscribeWithSchedulers(
            onSuccess = { blog -> viewState.onBlogUploaded(blog) },
            onError = { viewState.onError() }
        )
    }
}