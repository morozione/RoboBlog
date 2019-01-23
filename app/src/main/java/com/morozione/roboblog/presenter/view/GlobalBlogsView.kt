package com.morozione.roboblog.presenter.view

import com.arellomobile.mvp.MvpView
import com.morozione.roboblog.entity.Blog

interface GlobalBlogsView : MvpView {
    fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean)
    fun onRatingSuccess()
    fun onError()
}