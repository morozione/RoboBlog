package com.morozione.roboblog.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment

fun <T : View> AppCompatActivity.bind(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
fun <T : View> MvpAppCompatFragment.bind(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) { view!!.findViewById<T>(idRes) }

fun showSnackbar(view: View, message: String, duration: Int) {
    val snackBar = Snackbar.make(view, message, duration)
    snackBar.show()
}