package com.morozione.roboblog.ui.edituser

import android.app.Activity
import android.net.Uri
import com.morozione.roboblog.database.ImageUploadUtils
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.ui.shared.presenter.MvpBasePresenter
import moxy.InjectViewState

@InjectViewState
class EditUserPresenter : MvpBasePresenter<EditUserView>() {
    private val userDao = UserDao()

    val user = User()

    fun loadUser(userId: String) {
        userDao.loadUser(userId).subscribeWithSchedulers(
            onSuccess = { t ->
                // Copy loaded user data to presenter's user field
                user.id = t.id
                user.name = t.name
                user.email = t.email
                user.image = t.image
                user.rating = t.rating
                
                viewState.onUserLoaded(t)
            },
            onError = { viewState.onError() }
        )
    }

    fun updateUser(user: User) {
        userDao.updateUser(user).subscribeWithSchedulers(
            onComplete = { viewState.onUpdateSuccess(true) },
            onError = { viewState.onError() }
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