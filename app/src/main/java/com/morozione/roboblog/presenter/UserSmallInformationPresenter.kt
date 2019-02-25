package com.morozione.roboblog.presenter

import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.presenter.view.UserSmallInformationView
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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
