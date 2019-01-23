package com.morozione.roboblog.presenter

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

abstract class MvpBasePresenter<T : MvpView> : MvpPresenter<T>() {
    protected var compositeDisposable = CompositeDisposable()

    override fun attachView(view: T?) {
        super.attachView(view)
        compositeDisposable = CompositeDisposable()
    }

    override fun detachView(view: T?) {
        super.detachView(view)
        compositeDisposable.dispose()
    }
}