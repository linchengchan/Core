package com.cc.core.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cc.core.Core;

import java.util.Map;
import java.util.Set;

/**
 * 获取和设置SharedPreference的工具类
 */

public final class PrefUtils {

    /**
     * sp文件名
     */
    private static final String SHARED_PREF_NAME = "lvjia.sp.common";

    private static SharedPreferences getSP() {
        Context context = getContext();
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    private static Context getContext() {
        if (Core.sApp == null) {
            throw new NullPointerException("init first");
        }
        return Core.sApp;
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static String getString(String key, String defaultValue) {
        return getSP().getString(key, defaultValue);
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    public static long getLong(String key, long defaultValue) {
        return getSP().getLong(key, defaultValue);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public static float getFloat(String key, float defaultValue) {
        return getSP().getFloat(key, defaultValue);
    }


    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return getSP().getInt(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getSP().getBoolean(key, defaultValue);
    }

    public static Map<String, ?> getAll() {
        return getSP().getAll();
    }

    public static Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return getSP().getStringSet(key, defaultValue);
    }

    public static void putString(String key, String value) {
        getSP().edit().putString(key, value).apply();
    }

    public static void putLong(String key, long value) {
        getSP().edit().putLong(key, value).apply();
    }

    public static void putFloat(String key, float value) {
        getSP().edit().putFloat(key, value).apply();
    }

    public static void putInt(String key, int value) {
        getSP().edit().putInt(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        getSP().edit().putBoolean(key, value).apply();
    }

    public static void putStringSet(String key, Set<String> value) {
        getSP().edit().putStringSet(key, value).apply();
    }

}
