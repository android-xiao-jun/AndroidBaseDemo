package com.example.jun.base.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author Jun
 * @Description 子线程和主线程切换问题
 * @Time 2020/8/17
 */
public class HandlerUtil {
    private static HandlerUtil handlerUtil;

    public static HandlerUtil getInstance() {
        if (handlerUtil == null) {
            handlerUtil = new HandlerUtil();
        }
        return handlerUtil;
    }

    private Handler mainHandler;//主线程
    private Handler workHandler;//子线程

    public void init() {
        getInstance().mainHandler = new Handler(Looper.getMainLooper());
        HandlerThread thread = new HandlerThread("workHandler");
        thread.start();
        getInstance().workHandler = new Handler(thread.getLooper());
    }

    /**
     * 工作线程handler
     */
    public Handler getWorkHandler() {
        return workHandler;
    }

    /**
     * 获取主线程handler
     */
    public Handler getMainHandler() {
        return mainHandler;
    }

    public void addWorkHandlerRunnable(Runnable r) {
        if (r == null) return;
        getWorkHandler().post(r);
    }

    public void addMainHandlerRunnable(Runnable r) {
        if (r == null) return;
        getMainHandler().post(r);
    }

    public void removeRunnable() {
        getMainHandler().removeCallbacksAndMessages(null);
        getWorkHandler().removeCallbacksAndMessages(null);
    }
}
