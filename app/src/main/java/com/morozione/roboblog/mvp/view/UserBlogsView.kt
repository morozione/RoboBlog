package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView
import com.morozione.roboblog.entity.Blog

interface UserBlogsView : MvpView {
    fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean)
    fun onError()
    fun onDeleted()
}