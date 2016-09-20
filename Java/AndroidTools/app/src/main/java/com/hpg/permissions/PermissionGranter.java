package com.hpg.permissions;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.hpg.tools.Constant;
import com.unity3d.player.UnityPlayer;

/**
 * Created by altunin on 26.08.2016.
 */
public class PermissionGranter {
    public final static String UNITY_CALLBACK_GAMEOBJECT_NAME = "PermissionGranter";
    public final static String UNITY_CALLBACK_METHOD_NAME_GRANTED = "PermissionGrantedRequestCallbackInternal";
    public final static String UNITY_CALLBACK_METHOD_NAME_DENIED = "PermissionDeniedRequestCallbackInternal";
    private static int _requestCounter = 0;

    public static void grantPermission(String permission, String notificationMessage, String okButton) {
        if(permission.isEmpty())
        {
            Log.e(Constant.TAG, "Null permissions. You should specify permission.");
            return;
        }
        Log.i(Constant.TAG, "grantPermission " + permission);
        if (Build.VERSION.SDK_INT < 23) {
            Log.i("NoodlePermissionGranter", "Build.VERSION.SDK_INT < 23 (" + Build.VERSION.SDK_INT + ")");
            UnityPlayer.UnitySendMessage(UNITY_CALLBACK_GAMEOBJECT_NAME, UNITY_CALLBACK_METHOD_NAME_GRANTED, permission);
            return;
        }

        try {
            final int PERMISSIONS_REQUEST_CODE =(++_requestCounter);
            final String permissionName = permission;
            final String message = notificationMessage;
            final String ok = okButton;
            if (ContextCompat.checkSelfPermission(UnityPlayer.currentActivity, permissionName) == PackageManager.PERMISSION_GRANTED) {
                Log.i(Constant.TAG, "already granted");
                UnityPlayer.UnitySendMessage(UNITY_CALLBACK_GAMEOBJECT_NAME, UNITY_CALLBACK_METHOD_NAME_GRANTED, permissionName);
                return;
            }

            final FragmentManager fragmentManager = UnityPlayer.currentActivity.getFragmentManager();
            final Fragment request = new Fragment() {

                @Override
                public void onStart() {
                    super.onStart();
                    final String[] permissionsToRequest = new String[]{permissionName};
                    Log.i(Constant.TAG, "fragment start " + permissionsToRequest[0]);
                    if (shouldShowRequestPermissionRationale(permissionName)) {
                        final Fragment current = this;
                        AlertDialog.Builder builder = new AlertDialog.Builder(UnityPlayer.currentActivity);
                        builder.setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        requestPermissions(permissionsToRequest, PERMISSIONS_REQUEST_CODE);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        requestPermissions(permissionsToRequest, PERMISSIONS_REQUEST_CODE);
                    }
                }

                @Override
                public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                    Log.i(Constant.TAG, "onRequestPermissionsResult");
                    if (requestCode != PERMISSIONS_REQUEST_CODE)
                        return;
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.i(Constant.TAG, Constant.PERMISSION_GRANTED);
                        UnityPlayer.UnitySendMessage(UNITY_CALLBACK_GAMEOBJECT_NAME, UNITY_CALLBACK_METHOD_NAME_GRANTED, permissions[0]);
                    } else {
                        Log.i(Constant.TAG, Constant.PERMISSION_DENIED);
                        UnityPlayer.UnitySendMessage(UNITY_CALLBACK_GAMEOBJECT_NAME, UNITY_CALLBACK_METHOD_NAME_DENIED, permissions[0]);
                    }
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(this);
                    fragmentTransaction.commit();
                }
            };

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(0, request);
            fragmentTransaction.commit();
        } catch (Exception error) {
            Log.w(Constant.TAG, String.format("Unable to request permission: %s", error.getMessage()));
            UnityPlayer.UnitySendMessage(UNITY_CALLBACK_GAMEOBJECT_NAME, UNITY_CALLBACK_METHOD_NAME_DENIED, permission);
        }
    }
}
