package com.cc.core;

import android.app.Application;

import com.cc.core.global.DeviceParams;

public class Core {

    private static Application sApp;
    private static boolean DEBUG;

    public static void init(Application application) {
        init(application, false);
    }

    public static void init(Application application, boolean debug) {
        sApp = application;
        DeviceParams.init(application);
        DEBUG = debug;
    }

    public static boolean debug() {
        return DEBUG;
    }

    public static Application getApp() {
        return sApp;
    }
}
