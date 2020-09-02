package com.example.jun.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.example.jun.base.handler.HandlerUtil;
import com.example.jun.base.utils.SPutils;
import com.example.jun.base.utils.ToastUtils;
import com.hsjskj.hy.library_net.net.HttpHeaderConfig;
import com.hsjskj.hy.library_net.net.NetError;
import com.hsjskj.hy.library_net.net.NetProvider;
import com.hsjskj.hy.library_net.net.RequestHandler;
import com.hsjskj.hy.library_net.net.XApi;
import com.hsjskj.hy.library_net.net.XLog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * author        : Jun
 * time          : 2020年05月31日
 * description   : BaseProject
 */
public class MyApplication extends MultiDexApplication {

    private static Context context;
    private static Application instance;

    public static Context getContext() {
        return context;
    }

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ToastUtils.init(this);
        XLog.e("MyApplication_start" + System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        /* 下拉刷新 header 样式 */
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            return new ClassicsHeader(context);
        });

        /* 上拉加载  footer 样式*/
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context));


        context = this;
        HttpHeaderConfig.getInstance().setContext(this);
        HttpHeaderConfig.SP_Name = SPutils.getInstance().SP_Name;//配置文件 名称
        HttpHeaderConfig.KEY_Token = SPutils.getInstance().KEY_Token;//配置文件 token名称
        //网络请求配置
        XApi.registerProvider(new NetProvider() {

            @Override
            public Interceptor[] configInterceptors() {
                return new Interceptor[0];
            }

            @Override
            public void configHttps(OkHttpClient.Builder builder) {

            }

            @Override
            public CookieJar configCookie() {
                return null;
            }

            @Override
            public RequestHandler configHandler() {
                return null;
            }

            @Override
            public long configConnectTimeoutMills() {
                return 0;
            }

            @Override
            public long configReadTimeoutMills() {
                return 0;
            }

            @Override
            public boolean configLogEnable() {
                return true;
            }

            @Override
            public boolean handleError(NetError error) {
                return false;
            }

            @Override
            public boolean dispatchProgressEnable() {
                return false;
            }

            @Override
            public Context initContext() {
                return context;
            }
        });

        HandlerUtil.getInstance().init();
    }
}
