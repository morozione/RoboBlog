package com.morozione.roboblog.ui.shared.dialog

import android.app.AlertDialog
import android.content.Context
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
