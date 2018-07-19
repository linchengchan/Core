package com.cc.core.global;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadHelper {

    private static final ThreadPoolExecutor sExecutor;
    private static ThreadHelper sThreadHelper;

    static {
        sExecutor = new ThreadPoolExecutor(1, 5,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }

    private Handler mMainThreadHandler;
    private Handler mSingletonHandler;
    private HandlerThread mHandlerThread;

    public static Looper getSingletonHandlerLooper() {
        return getInstance().mHandlerThread.getLooper();
    }

    public static void runOnSingletonHandlerThread(Runnable runnable) {
        getInstance().mSingletonHandler.post(runnable);
    }

    public static void runOnSingletonHandlerThreadDelay(Runnable runnable, long delay) {
        getInstance().mSingletonHandler.postDelayed(runnable, delay);
    }

    private ThreadHelper() {
        mHandlerThread = new HandlerThread("SingletonHandlerThread");
        mHandlerThread.start();
        mSingletonHandler = new Handler(mHandlerThread.getLooper());
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 请勿直接使用{@link #sThreadHelper}获取ThreadHelper实例。
     * 请使用此方法来获取实例，目的是可以实现懒加载
     */
    private static ThreadHelper getInstance() {
        if (sThreadHelper == null) {
            synchronized (ThreadHelper.class) {
                if (sThreadHelper == null) {
                    sThreadHelper = new ThreadHelper();
                }
            }
        }
        return sThreadHelper;
    }

    /**
     * 当前线程是否为主线程
     */
    public static boolean isUiThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * 在主线程中运行
     */
    public static void runOnUiThread(Runnable runnable) {
        getInstance().mMainThreadHandler.post(runnable);
    }

    /**
     * 如果运行在主线程，则立即执行
     */
    public static void runOnUiThreadImmediate(Runnable runnable) {
        if (isUiThread()) {
            runnable.run();
        } else {
            getInstance().mMainThreadHandler.post(runnable);
        }
    }

    /**
     * 延迟一定时间后在主线程执行任务
     */
    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis) {
        getInstance().mMainThreadHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 在工作线程中执行任务
     */
    public static void runOnWorkThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }


}
