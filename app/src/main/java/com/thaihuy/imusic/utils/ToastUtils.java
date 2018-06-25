package com.thaihuy.imusic.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public final class ToastUtils {

    public static void quickToast(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, duration);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        if (textView != null) {
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 0, 10, 0);
        }
        toast.show();
    }

    private ToastUtils() {
    }
}
