package com.morozione.roboblog.presenter

import com.arellomobile.mvp.InjectViewState
import com.google.firebase.auth.FirebaseUser
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.presenter.view.LoginView
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter : MvpBasePresenter<LoginView>() {

    private val userDao = UserDao()

    val currentUser: FirebaseUser?
        get() = userDao.currentUser

    fun singUp(login: String, password: String) {
        userDao.signUp(login, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    viewState.onRegistrationResult(true)
                }

                override fun onError(e: Throwable) {
                    viewState.onError()
                }
            })
    }

    fun singIn(login: String, password: String) {
        userDao.signIn(login, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    viewState.onAuthorizationResult(true)
                }

                override fun onError(e: Throwable) {
                    viewState.onError()
                }
            })
    }

    fun saveUser(user: User) {
        userDao.saveUser(user)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    viewState.onSavedUserSuccess(true)
                }

                override fun onError(e: Throwable) {
                    viewState.onError()
                }
            })
    }
}
