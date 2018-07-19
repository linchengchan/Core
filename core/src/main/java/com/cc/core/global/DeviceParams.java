package com.cc.core.global;


import android.app.Application;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.lvjia.core.BuildConfig;
import com.lvjia.core.util.PrefUtils;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 全局参数
 */
public class DeviceParams {

    /**
     * 生成的设备唯一id
     */
    private static final String KEY_DEVICE_ID = "key.device.id";

    private static final String TAG = DeviceParams.class.getSimpleName();

    private static boolean sInit;

    private static int sScreenWidth;
    private static int sScreenHeight;
    private static String sDeviceId;
    /**
     * 是否已经检查过版本更新情况
     */
    private static boolean sEverCheckVersion;

    private static boolean sExitNavigationBar;

    /**
     * 状态栏高度
     */
    private static int sStatusBarHeight;
    /**
     * app主题属性里标题栏高度
     */
    private static float sActionBarSize;
    /**
     * 手机导航栏高度
     */
    private static int sNavigationBarHeight;
    /**
     * 点击有水波纹的背景ID
     */
    private static int sSelectableItemBgResId;


    public static void init(Application application) {
        if (sInit) {
            return;
        }
        sInit = true;

        Resources r = application.getResources();

        //状态栏高度
        int statusBarResId = r.getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarResId > 0) {
            sStatusBarHeight = r.getDimensionPixelSize(statusBarResId);
        }
        //是否存在导航栏
        sExitNavigationBar = checkDeviceHasNavigationBar(r);
        if (sExitNavigationBar) {
            int navigationBarResId = r.getIdentifier("navigation_bar_height", "dimen", "android");
            sNavigationBarHeight = r.getDimensionPixelSize(navigationBarResId);
        }
        //标题栏高度
        TypedValue tv = new TypedValue();
        Resources.Theme appTheme = application.getTheme();
        appTheme.resolveAttribute(android.R.attr.actionBarSize, tv, true);
        sActionBarSize = r.getDimension(tv.resourceId);

        appTheme.resolveAttribute(android.R.attr.selectableItemBackground, tv, true);
        sSelectableItemBgResId = tv.resourceId;

        DisplayMetrics displayMetrics = r.getDisplayMetrics();
        sScreenWidth = displayMetrics.widthPixels;
        sScreenHeight = displayMetrics.heightPixels;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "init()-> ");
            Log.d(TAG, "getScreenWidth()-> screen width: " + sScreenWidth + " height: " + sScreenHeight);
            Log.d(TAG, "init()-> sStatusBarHeight: " + sStatusBarHeight);
            Log.d(TAG, "init()-> sExitNavigationBar: " + sExitNavigationBar);
            Log.d(TAG, "init()-> sNavigationBarHeight: " + sNavigationBarHeight);
        }

        //设备随机数Id
        sDeviceId = PrefUtils.getString(KEY_DEVICE_ID);
        if (TextUtils.isEmpty(sDeviceId)) {
            sDeviceId = UUID.randomUUID().toString();
            PrefUtils.putString(KEY_DEVICE_ID, sDeviceId);
        }
    }

    /**
     * 获取是否存在NavigationBar
     */
    private static boolean checkDeviceHasNavigationBar(Resources rs) {
        boolean hasNavigationBar = false;
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception ignore) {

        }
        return hasNavigationBar;

    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        return sScreenWidth;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static int getStatusBarHeight() {
        return sStatusBarHeight;
    }

    public static boolean everCheckVersion() {
        return sEverCheckVersion;
    }

    public static boolean isExitNavigationBar() {
        return sExitNavigationBar;
    }

    public static int getNavigationBarHeight() {
        return sNavigationBarHeight;
    }

    public static void setEverCheckVersion(boolean ever) {
        sEverCheckVersion = ever;
    }

    public static String getDeviceId() {
        return sDeviceId;
    }

    public static float getActionBarSize() {
        return sActionBarSize;
    }

    public static int getSelectableItemBgResId() {
        return sSelectableItemBgResId;
    }
}
