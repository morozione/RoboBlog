package com.morozione.roboblog.ui.edituser

import android.app.Activity
import android.net.Uri
import moxy.InjectViewState
import com.morozione.roboblog.database.ImageUploadUtils
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

@InjectViewState
class EditUserPresenter : MvpBasePresenter<EditUserView>() {
    private val userDao = UserDao()

    val user = User()

    fun loadUser(userId: String) {
        compositeDisposable.add(
            userDao.loadUser(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { t ->
                        // Copy loaded user data to presenter's user field
                        user.id = t.id
                        user.name = t.name
                        user.email = t.email
                        user.image = t.image
                        user.rating = t.rating
                        
                        viewState.onUserLoaded(t)
                    },
                    { viewState.onError() }
                )
        )
    }

    fun updateUser(user: User) {
        compositeDisposable.add(
            userDao.updateUser(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState.onUpdateSuccess(true) },
                    { viewState.onError() }
                )
        )
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

    fun signOut() {
        userDao.signOut()
        viewState.onLogoutSuccess()
    }
}