package com.morozione.roboblog.presenter.view

import com.arellomobile.mvp.MvpView

interface LoginView : MvpView {
    fun onAuthorizationResult(isSuccess: Boolean)
    fun onRegistrationResult(isSuccess: Boolean)
    fun onSavedUserSuccess(isSuccess: Boolean)
    fun onError()
}
