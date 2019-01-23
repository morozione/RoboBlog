package com.morozione.roboblog.presenter.view

import com.arellomobile.mvp.MvpView
import com.morozione.roboblog.entity.User

interface EditUserView : MvpView {
    fun onUserLoaded(user: User)
    fun onUpdateSuccess(isSuccess: Boolean)
    fun onError()
}