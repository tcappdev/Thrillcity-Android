package com.android.mobile.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.HashMap;

public class UIUtils {

    private static HashMap<String, Toast> toastMap = new HashMap<>();

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null && view.getWindowToken() != null) {
            final InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(View view) {
        if (view != null && view.getWindowToken() != null) {
            final InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(View view) {
        if (view != null && view.getWindowToken() != null) {
            final InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void startActivity(View view, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            final int w = view.getWidth();
            final int h = view.getHeight();
            final ActivityOptions opts
                    = ActivityOptions.makeScaleUpAnimation(view, w / 2, h / 2, w, h);
            view.getContext().startActivity(intent, opts.toBundle());
        } else {
            view.getContext().startActivity(intent);
        }
    }

    public static void startActivity(Activity activity, MenuItem item, Intent intent) {
        final View view = activity.findViewById(item.getItemId());
        if (view == null) {
            activity.startActivity(intent);
        } else {
            startActivity(view, intent);
        }
    }

    public static void showToastMessage(String message, Context context) {
        checkToast(message);
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMap.put(message, toast);
        toast.show();
    }

    public static void showToastMessage(int message, Context context) {
        checkToast(Integer.toString(message));
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMap.put(Integer.toString(message), toast);
        toast.show();
    }

    /*
     * get (or create) toast for this key, cancelling any with same message (key)
     */
    protected static Toast checkToast(String key) {
        Toast toast = getToast(key);
        if (toast != null) {
            toast.cancel();
            Log.d("Cancelling Toast " + toast.toString());
        }

        return toast;
    }

    protected static void setToast(String key, Toast toast) {
        toastMap.put(key, toast);
    }

    protected static Toast getToast(String message) {
        return toastMap.get(message);
    }

    public static void gotoEmail(String emailAddress, Activity fragmentActivity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        Intent mailer = Intent.createChooser(intent, "Please select an email client");
        fragmentActivity.startActivity(mailer);
    }

}
