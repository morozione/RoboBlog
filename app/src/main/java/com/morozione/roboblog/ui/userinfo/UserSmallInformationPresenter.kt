package com.morozione.roboblog.ui.userinfo

import moxy.InjectViewState
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.ui.userinfo.UserSmallInformationView
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class UserSmallInformationPresenter : MvpBasePresenter<UserSmallInformationView>() {
    val userDao = UserDao()

    fun loadUser(userId: String) {
        compositeDisposable.add(
            userDao.loadUser(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { user -> viewState.setUser(user) },
                    { viewState.onError() }
                )
        )
    }
}
