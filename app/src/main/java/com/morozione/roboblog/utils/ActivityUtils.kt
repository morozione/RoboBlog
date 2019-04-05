package com.morozione.roboblog.utils

import android.support.annotation.IdRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment

fun <T : View> AppCompatActivity.bind(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
fun <T : View> MvpAppCompatFragment.bind(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) { view!!.findViewById<T>(idRes) }

fun showSnackbar(view: View, message: String, duration: Int) {
    val snackBar = Snackbar.make(view, message, duration)
    snackBar.show()
}