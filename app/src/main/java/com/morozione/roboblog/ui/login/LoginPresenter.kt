package com.morozione.roboblog.ui.login

import com.google.firebase.auth.FirebaseUser
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState


@InjectViewState
class LoginPresenter : MvpBasePresenter<LoginView>() {

    private val userDao = UserDao()

    val currentUser: FirebaseUser?
        get() = userDao.currentUser

    fun singUp(login: String, password: String) {
        userDao.signUp(login, password).subscribeWithSchedulers(
            onComplete = { viewState.onRegistrationResult(true) },
            onError = { viewState.onError() }
        )
    }

    fun singIn(login: String, password: String) {
        userDao.signIn(login, password).subscribeWithSchedulers(
            onComplete = { viewState.onAuthorizationResult(true) },
            onError = { viewState.onError() }
        )
    }

    fun saveUser(user: User) {
        userDao.saveUser(user).subscribeWithSchedulers(
            onComplete = { viewState.onSavedUserSuccess(true) },
            onError = { viewState.onError() }
        )
    }
}
