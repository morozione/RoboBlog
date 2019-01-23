package com.morozione.roboblog.utils

import android.annotation.SuppressLint

import java.text.SimpleDateFormat


object Utils {

    fun getDate(time: Long): String {
        @SuppressLint("SimpleDateFormat")
        val tateFormat = SimpleDateFormat("dd:MM:yy")
        return tateFormat.format(time)
    }

    fun getTime(time: Long): String {
        @SuppressLint("SimpleDateFormat")
        val timeFormat = SimpleDateFormat("HH:mm")
        return timeFormat.format(time)
    }

    fun getFullDate(time: Long): String {
        @SuppressLint("SimpleDateFormat")
        val fullFormat = SimpleDateFormat("dd:MM:yy  HH:mm")
        return fullFormat.format(time)
    }

    fun noNull(s: String?) = s ?: ""
}
