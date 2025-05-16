package com.morozione.roboblog.core

import android.util.Log
import com.morozione.roboblog.App
import java.util.*

class OffirentPresenterStorage {
    private val TAG = OffirentPresenterStorage::class.java.simpleName

    private val presenters = HashMap<String, Presenter>()

    fun storagePresenter(tag: String, presenter: Presenter) {
        Log.d(TAG, ">> storePresenter, presenter = \$presenter, tag = \$tag")
        presenters[tag] = presenter
    }

    fun getPresenter(tag: String): Presenter? {
        Log.d(TAG, ">> getPresenter, presenter's tag = \$tag")

        return presenters[tag]
    }

    fun clear(tag: String) {
        Log.d(TAG, ">> clearPresenter, presenter's tag = \$tag")
        presenters.remove(tag)
    }

    companion object {
        val instance: OffirentPresenterStorage
            get() = App.presenterStorage
    }
}
