package com.devabhi.circlecue;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class MyPermissionClass {

    private Activity activity;
    public final int CAMERA_PERMISSION_CODE = 9001;
    public final int LOCATION_PERMISSION_CODE = 9002;
    public final int CALL_PERMISSION_CODE = 9002;

    public MyPermissionClass(Activity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean camera() {
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            activity.requestPermissions(permission, CAMERA_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    public boolean resultCamera(int requestCode, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            boolean camera = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean externalStorage = grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED;
            return camera && externalStorage;
        }
        return false;
    }

}
