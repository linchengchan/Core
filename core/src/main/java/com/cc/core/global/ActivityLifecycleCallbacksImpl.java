package com.cc.core.global;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.cc.core.BuildConfig;

import java.util.ArrayList;

public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = ActivityLifecycleCallbacksImpl.class.getSimpleName();

    private static ActivityLifecycleCallbacksImpl sLifecycleCallbacks;


    public static void init(Application application) {
        if (sLifecycleCallbacks == null) {
            synchronized (ActivityLifecycleCallbacksImpl.class) {
                if (sLifecycleCallbacks == null) {
                    sLifecycleCallbacks = new ActivityLifecycleCallbacksImpl();
                    application.registerActivityLifecycleCallbacks(sLifecycleCallbacks);
                }
            }
        }
    }

    private static ActivityLifecycleCallbacksImpl get() {
        if (sLifecycleCallbacks == null) {
            throw new NullPointerException();
        }
        return sLifecycleCallbacks;
    }

    private final ArrayList<Activity> activityList = new ArrayList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activityList.add(activity);
        if (DEBUG) {
            Log.d(TAG, "onActivityCreated()-> create a activity: " + activity.getClass().getCanonicalName());
            for (Activity a : activityList) {
                Log.d(TAG, "onActivityCreated()-> activity: " + a.getClass().getCanonicalName());
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        boolean remove = activityList.remove(activity);
        if (DEBUG) {
            if (remove) {
                Log.d(TAG, "onActivityDestroyed()-> remove activity " + activity.getClass().getCanonicalName());
            } else {
                Log.d(TAG, "onActivityDestroyed()-> not find activity in list");
            }
            for (Activity a : activityList) {
                Log.d(TAG, "onActivityDestroyed()-> activity: " + a.getClass().getCanonicalName());
            }
        }

    }

    public static void closeAllActivity(String excludeActivityCanonicalName) {
        ArrayList<Activity> activityList = get().activityList;
        int length = activityList.size();
        for (int i = length - 1; i >= 0; i--) {
            Activity activity = activityList.get(i);
            if (!activity.getClass().getCanonicalName().equals(excludeActivityCanonicalName)) {
                activity.finish();
            }
        }
    }

    public static Activity getActivity(String activityCanonicalName) {

        Activity activity = null;

        ArrayList<Activity> activityList = get().activityList;
        int length = activityList.size();
        for (int i = length - 1; i >= 0; i--) {
            Activity a = activityList.get(i);
            if (a.getClass().getCanonicalName().equals(activityCanonicalName)) {
                activity = a;
                break;
            }
        }

        return activity;
    }

}
