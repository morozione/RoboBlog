package com.morozione.roboblog.ui.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.morozione.roboblog.R;

import java.util.Calendar;

public class DialogFactory {
    public interface OnDialogChangeDataListener {
        void onDataChanged(Calendar calendar);
    }

    public static MaterialDialog.Builder createDialogSelectMakingImage(
            Context context, MaterialDialog.ListCallback listCallback) {

        return new MaterialDialog.Builder(context)
                .items(context.getString(R.string.gallery), context.getString(R.string.camera))
                .itemsCallback(listCallback);
    }

}
