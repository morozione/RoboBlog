package com.morozione.roboblog.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.morozione.roboblog.R

object FragmentUtil {
    fun changeFragmentTo(activity: FragmentActivity, fragment: Fragment, TAG: String) {
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.m_container, fragment, TAG)
                .addToBackStack(TAG)
                .commit()
    }
}
