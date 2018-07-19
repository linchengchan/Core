package com.cc.core.global;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.lvjia.core.BuildConfig;
import com.lvjia.core.R;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {

    private Activity activity;

    private Fragment fragment;

    private int requestCode;

    private boolean jumpAppDetail;

    private String[] permissions;

    private Runnable[] grantActions;

    private Runnable[] deniedActions;

    private PermissionHelper(Builder builder) {
        activity = builder.activity;
        fragment = builder.fragment;
        requestCode = builder.requestCode;
        jumpAppDetail = builder.jumpAppDetail;
        permissions = builder.permissions
                .toArray(new String[builder.permissions.size()]);
        grantActions = builder.grantActions
                .toArray(new Runnable[builder.grantActions.size()]);
        deniedActions = builder.deniedActions
                .toArray(new Runnable[builder.deniedActions.size()]);
        request();

    }

    private void request() {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else if (fragment != null) {
            fragment.requestPermissions(permissions, requestCode);
        } else {
            throw new NullPointerException();
        }
    }

    private void shouldShowRequestPermissionRationaleHandle(String permission, final Runnable action) {
        String title = "";
        String msg = "";
        Resources r = activity == null ? fragment.getResources() : activity.getResources();
        String[] titles = r.getStringArray(R.array.permission_titles);
        String[] messages = r.getStringArray(R.array.permission_messages);
        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission) ||
                Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
            title = titles[0];
            msg = messages[0];
        } else if (Manifest.permission.CAMERA.equals(permission)) {
            title = titles[1];
            msg = messages[1];
        } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission) ||
                Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            title = titles[2];
            msg = messages[2];
        } else if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
            title = titles[3];
            msg = messages[3];
        }

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(msg)) {
            action.run();
            return;
        }

        Activity activity = this.activity;
        if (activity == null && fragment != null) {
            activity = fragment.getActivity();
        }
        if (activity == null) {
            throw new NullPointerException();
        }
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (jumpAppDetail) {
                            jumpToAppSetting();
                        }
                        if (action != null) {
                            action.run();
                        }
                    }
                }).show();
    }

    /**
     * 跳转到应用设置详情页
     */
    private void jumpToAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
        intent.setData(uri);
        Activity activity = this.activity;
        if (activity == null) {
            activity = fragment.getActivity();
        }
        if (activity == null) {
            throw new NullPointerException();
        }
        activity.startActivity(intent);
    }


    private void deniedAfter(String permission, Runnable action) {
        boolean show;
        if (activity == null) {
            show = fragment.shouldShowRequestPermissionRationale(permission);
        } else {
            show = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (!show) {
            shouldShowRequestPermissionRationaleHandle(permission, action);
        } else {
            if (action != null) {
                action.run();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == this.requestCode) {
            int length = grantResults.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Runnable grantAction = grantActions[i];
                    if (grantAction != null) {
                        grantAction.run();
                    }
                } else {
                    Runnable deniedAction = deniedActions[i];
                    deniedAfter(permissions[i], deniedAction);
                }
            }
        }
    }


    public static class Builder {

        private final String TAG = getClass().getSimpleName();
        private final boolean DEBUG = BuildConfig.DEBUG;

        private final Activity activity;

        private final Fragment fragment;

        private final int requestCode;

        private boolean jumpAppDetail = true;

        private final List<String> permissions = new ArrayList<>(1);

        private final List<Runnable> grantActions = new ArrayList<>(1);

        private final List<Runnable> deniedActions = new ArrayList<>(1);

        public Builder(Activity activity, int requestCode) {
            this.activity = activity;
            this.fragment = null;
            this.requestCode = requestCode;
        }

        public Builder(Fragment fragment, int requestCode) {
            this.activity = null;
            this.fragment = fragment;
            this.requestCode = requestCode;
        }

        public Builder jumpAppDetail(boolean jump) {
            jumpAppDetail = jump;
            return this;
        }

        public Builder cameraPermission(Runnable grantAction, Runnable deniedAction) {
            permission(Manifest.permission.CAMERA, grantAction, deniedAction);
            return this;
        }

        public Builder locationPermission(Runnable grantAction, Runnable deniedAction) {
            permission(Manifest.permission.ACCESS_COARSE_LOCATION, grantAction, deniedAction);
            return this;
        }

        public Builder externalStoragePermission(Runnable grantAction, Runnable deniedAction) {
            permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, grantAction, deniedAction);
            return this;
        }

        public Builder recordAudioPermission(Runnable grantAction, Runnable deniedAction) {
            permission(Manifest.permission.RECORD_AUDIO, grantAction, deniedAction);
            return this;
        }


        public Builder permission(String permission, Runnable grantAction, Runnable deniedAction) {
            if (!permissions.contains(permission)) {
                permissions.add(permission);
                grantActions.add(grantAction);
                deniedActions.add(deniedAction);
            }
            return this;
        }

        public Builder permission(String[] permission, @Nullable Runnable[] grantAction, @Nullable Runnable[] deniedAction) {
            if (permission == null || permission.length == 0) {
                if (DEBUG) {
                    Log.e(TAG, "permission()-> permission array is null or empty");
                }
                return this;
            }
            if (grantAction != null && grantAction.length != permission.length) {
                throw new IllegalArgumentException();
            }

            if (deniedAction != null && deniedAction.length != permission.length) {
                throw new IllegalArgumentException();
            }
            int length = permission.length;
            int grantLength = grantAction == null ? 0 : grantAction.length;
            int deniedLength = deniedAction == null ? 0 : deniedAction.length;

            for (int i = 0; i < length; i++) {
                Runnable gA = i < grantLength ? grantAction[i] : null;
                Runnable dA = i < deniedLength ? deniedAction[i] : null;
                permission(permission[i], gA, dA);
            }
            return this;
        }


        public PermissionHelper build() {
            if (permissions.isEmpty()) {
                throw new NullPointerException();
            }
            return new PermissionHelper(this);
        }

    }

}
