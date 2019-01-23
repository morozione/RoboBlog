package com.morozione.roboblog.presenter.view

import com.arellomobile.mvp.MvpView
import com.morozione.roboblog.entity.Blog

interface BlogView : MvpView {
    fun onBlogUploaded(blog: Blog)
    fun onError()
}