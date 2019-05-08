package com.morozione.roboblog.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.morozione.roboblog.R

fun FragmentActivity.changeFragmentTo(fragment: Fragment, TAG: String) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.m_container, fragment, TAG)
        .addToBackStack(TAG)
        .commit()
}

fun FragmentActivity.changeFragmentTo(container: Int, fragment: Fragment, TAG: String) {
    supportFragmentManager.beginTransaction()
        .replace(container, fragment, TAG)
        .addToBackStack(TAG)
        .commit()
}