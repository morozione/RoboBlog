package com.morozione.roboblog.mvp.presenter

import moxy.MvpPresenter
import moxy.MvpView
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class MvpBasePresenter<T : MvpView> : MvpPresenter<T>() {
    protected var compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}