package com.morozione.roboblog.ui.userinfo

import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState


@InjectViewState
class UserSmallInformationPresenter : MvpBasePresenter<UserSmallInformationView>() {
    val userDao = UserDao()

    fun loadUser(userId: String) {
        userDao.loadUser(userId).subscribeWithSchedulers(
            onSuccess = { user -> viewState.setUser(user) },
            onError = { viewState.onError() }
        )
    }
}
