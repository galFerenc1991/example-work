package com.kubator.pamp.presentation.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.kubator.pamp.PampApp_;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class ToastManager {

    private static Toast mToast = Toast.makeText(PampApp_.getInstance().getApplicationContext(), "", Toast.LENGTH_LONG);

    public static void showToast(CharSequence message) {
        mToast.setText(message);
        mToast.show();
    }

    public static void showToast(@StringRes int resId) {
        mToast.setText(resId);
        mToast.show();
    }
}
