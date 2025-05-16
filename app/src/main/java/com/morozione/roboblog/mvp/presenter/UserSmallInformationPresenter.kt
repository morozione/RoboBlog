package com.morozione.roboblog.mvp.presenter

import moxy.InjectViewState
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.mvp.view.UserSmallInformationView
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class UserSmallInformationPresenter : MvpBasePresenter<UserSmallInformationView>() {
    val userDao = UserDao()

    fun loadUser(userId: String) {
        userDao.loadUser(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        viewState.setUser(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        viewState.onError()
                    }
                })
    }
}
