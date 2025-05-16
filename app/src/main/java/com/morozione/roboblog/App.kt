package com.morozione.roboblog

import android.app.Application
import com.google.firebase.FirebaseApp

import com.google.firebase.database.FirebaseDatabase
import com.morozione.roboblog.core.OffirentPresenterStorage

class App : Application() {

    companion object {
        val presenterStorage = OffirentPresenterStorage()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
