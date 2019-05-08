package com.morozione.roboblog.mvp.presenter

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

abstract class MvpBasePresenter<T : MvpView> : MvpPresenter<T>() {
    protected var compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}