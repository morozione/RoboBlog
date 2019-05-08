package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView

interface CreateBlogView : MvpView {
    fun onBlogCreated()
    fun onError()
    fun onBlogUpdated()
}