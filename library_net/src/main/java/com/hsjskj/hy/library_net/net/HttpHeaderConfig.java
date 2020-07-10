package com.hsjskj.hy.library_net.net;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Wen xiao
 * @time 2020/3/23
 */
public class HttpHeaderConfig {
    public Context context;

    public static String SP_Name = "hy";
    public static String KEY_Token = "token";  // token

    static HttpHeaderConfig instance;

    public static HttpHeaderConfig getInstance() {
        if (instance == null) instance = new HttpHeaderConfig();
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public String getStringFromSp(String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        return sp.getString(key, "");

    }
}
