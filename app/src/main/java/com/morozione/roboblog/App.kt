package com.morozione.roboblog

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.morozione.roboblog.core.OffirentPresenterStorage

class App : Application() {

    companion object {
        val presenterStorage = OffirentPresenterStorage()
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase using KTX syntax
        Firebase.database.setPersistenceEnabled(true)
    }
}
