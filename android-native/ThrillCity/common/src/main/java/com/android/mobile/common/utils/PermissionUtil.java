package com.android.mobile.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PermissionUtil {

    public static final int LOCATION_PERMISSION = 1;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestLocationPermission(Activity activity) {
        String[] permissions = new String[]{ACCESS_FINE_LOCATION};
        activity.requestPermissions(permissions, LOCATION_PERMISSION);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestLocationPermissionIfNotDenied(Activity activity) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)) {
            requestLocationPermission(activity);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean shouldAskForLocationPermission(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                ACCESS_FINE_LOCATION);
    }

    public static boolean isAndroidMWithModifySettingsRequirementDefect() {
        return (Build.VERSION.RELEASE.equals("6.0"));
    }
}
