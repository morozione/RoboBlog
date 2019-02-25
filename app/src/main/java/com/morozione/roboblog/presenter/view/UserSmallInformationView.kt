package com.morozione.roboblog.presenter.view

import com.arellomobile.mvp.MvpView
import com.morozione.roboblog.entity.User

interface UserSmallInformationView : MvpView {
    fun setUser(user: User)
    fun onError()
}