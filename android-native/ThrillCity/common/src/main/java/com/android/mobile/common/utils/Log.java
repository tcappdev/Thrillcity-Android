package com.android.mobile.common.utils;

public class Log {

    private static final String TAG = "Common";
    private static final boolean DEBUG = true;

    public static void i(String message) {
        if (DEBUG) {
            android.util.Log.i(TAG, message);
        }
    }

    public static void d(String message) {
        if (DEBUG) {
            android.util.Log.d(TAG, message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            android.util.Log.e(TAG, message);
        }
    }

    public static void e(Exception e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }

    public static void v(String message) {
        if (DEBUG) {
            android.util.Log.v(TAG, message);
        }
    }

}
