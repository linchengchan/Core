package com.cc.core;

import android.app.Application;

import com.cc.core.global.DeviceParams;

public class Core {

    public static Application sApp;

    public static void init(Application application) {
        sApp = application;
        DeviceParams.init(application);
    }
}
