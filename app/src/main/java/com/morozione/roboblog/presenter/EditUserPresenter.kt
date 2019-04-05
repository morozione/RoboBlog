package com.morozione.roboblog.presenter

import android.app.Activity
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.morozione.roboblog.database.ImageUploadUtils
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.presenter.view.EditUserView
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

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

    fun saveUser(activity: Activity, imageUri: Uri?, newUser: User) {
        if (imageUri != null && imageUri.toString().isNotEmpty()) {
            val imageUploadUtils = ImageUploadUtils(activity, compositeDisposable)
            val patchs = ArrayList<String>()
            patchs.add(imageUri.toString())
            imageUploadUtils.uploadImages(patchs, newUser.id) { patchs1 ->
                newUser.image = patchs1[0]
                updateUser(newUser)
            }
        } else
            updateUser(newUser)
    }
}