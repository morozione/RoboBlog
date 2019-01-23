package com.morozione.azotova.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.morozione.roboblog.R


object FragmentUtil {
    fun changeFragmentTo(activity: FragmentActivity, fragment: Fragment, TAG: String) {
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, TAG)
                .addToBackStack(TAG)
                .commit()
    }
}
