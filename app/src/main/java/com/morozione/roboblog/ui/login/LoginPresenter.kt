package com.morozione.roboblog.ui.login

import moxy.InjectViewState
import com.google.firebase.auth.FirebaseUser
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class LoginPresenter : MvpBasePresenter<LoginView>() {

    private val userDao = UserDao()

    val currentUser: FirebaseUser?
        get() = userDao.currentUser

    fun singUp(login: String, password: String) {
        compositeDisposable.add(
            userDao.signUp(login, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState.onRegistrationResult(true) },
                    { viewState.onError() }
                )
        )
    }

    fun singIn(login: String, password: String) {
        compositeDisposable.add(
            userDao.signIn(login, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState.onAuthorizationResult(true) },
                    { viewState.onError() }
                )
        )
    }

    fun saveUser(user: User) {
        compositeDisposable.add(
            userDao.saveUser(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState.onSavedUserSuccess(true) },
                    { viewState.onError() }
                )
        )
    }
}
