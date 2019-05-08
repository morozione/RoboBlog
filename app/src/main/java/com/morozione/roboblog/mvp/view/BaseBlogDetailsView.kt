package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView
import com.morozione.roboblog.entity.Blog

interface BaseBlogDetailsView : MvpView {
    fun onBlogUploaded(blog: Blog)
    fun onError()
}