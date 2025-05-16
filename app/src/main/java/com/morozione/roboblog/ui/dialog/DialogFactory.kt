package com.morozione.roboblog.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialDialogs
import com.morozione.roboblog.R
import java.util.Calendar

object DialogFactory {
    fun createDialogSelectMakingImage(
        context: Context,
        onItemClicked: (position: Int) -> Unit
    ): AlertDialog.Builder {
        return AlertDialog.Builder(context)
            .setItems(
                listOf(
                    context.getText(R.string.gallery),
                    context.getText(R.string.camera)
                ).toTypedArray(),
                { _, which ->
                    onItemClicked(which)
                })
    }

    interface OnDialogChangeDataListener {
        fun onDataChanged(calendar: Calendar?)
    }
}
