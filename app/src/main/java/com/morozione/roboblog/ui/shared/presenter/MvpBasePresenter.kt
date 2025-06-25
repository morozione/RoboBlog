package com.morozione.roboblog.ui.shared.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import moxy.MvpView

abstract class MvpBasePresenter<T : MvpView> : MvpPresenter<T>() {
    protected var compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

    // Extension function for Single
    protected fun <R : Any> Single<R>.subscribeWithSchedulers(
        onSuccess: (R) -> Unit,
        onError: (Throwable) -> Unit = { }
    ) {
        compositeDisposable.add(
            this.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError)
        )
    }

    // Extension function for Completable
    protected fun Completable.subscribeWithSchedulers(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit = { }
    ) {
        compositeDisposable.add(
            this.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError)
        )
    }

    // Extension function for Observable
    protected fun <R : Any> Observable<R>.subscribeWithSchedulers(
        onNext: (R) -> Unit,
        onError: (Throwable) -> Unit = { },
        onComplete: () -> Unit = { }
    ) {
        compositeDisposable.add(
            this.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete)
        )
    }
}