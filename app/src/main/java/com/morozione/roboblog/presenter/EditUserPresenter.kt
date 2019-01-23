package com.morozione.roboblog.presenter

import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.presenter.view.EditUserView
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class EditUserPresenter : MvpBasePresenter<EditUserView>() {
    private val userDao = UserDao()

    val user = User()

    fun loadUser(userId: String) {
        userDao.loadUser(userId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<User> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: User) {
                    viewState.onUserLoaded(t)
                }

                override fun onError(e: Throwable) {
                    viewState.onError()
                }
            })
    }

    fun updateUser(user: User) {
        userDao.updateUser(user)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    viewState.onUpdateSuccess(true)
                }

                override fun onError(e: Throwable) {
                    viewState.onError()
                }
            })
    }
}