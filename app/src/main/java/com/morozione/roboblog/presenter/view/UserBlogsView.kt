package com.morozione.roboblog.presenter.view

import com.arellomobile.mvp.MvpView
import com.morozione.roboblog.entity.Blog

interface UserBlogsView : MvpView {
    fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean)
    fun onError()
}