package com.morozione.roboblog.presenter.view

import com.arellomobile.mvp.MvpView

interface CreateBlogView : MvpView {
    fun onBlogCreated()
    fun onError()
    fun onBlogUpdated()
}