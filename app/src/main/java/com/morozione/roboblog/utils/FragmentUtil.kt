package com.morozione.roboblog.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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