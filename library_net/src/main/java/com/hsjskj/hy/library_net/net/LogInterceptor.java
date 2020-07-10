package com.hsjskj.hy.library_net.net;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by wanglei on 2017/1/7.
 */

public class LogInterceptor implements Interceptor {
    public static final String TAG = "XDroid_Net";

    @Override
    public Response intercept(Chain chain) throws IOException {
        //读取本地保存的SessionId
//        String sessionId = SPutils.getInstance().getStringFromSp(MyApplication.getInstance(), "SessionId");
//        XLog.e("COOKIES读取", sessionId);
        Request.Builder builder = chain.request().newBuilder();
//        if (sessionId != null && !"".equals(sessionId)) {
//            builder.addHeader("Cookie", (sessionId != null ? sessionId : ""));
//        }

        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Token", HttpHeaderConfig.getInstance().getStringFromSp(HttpHeaderConfig.KEY_Token));
//        builder.addHeader("Accept","application/json");
        Request request = builder.build();
//        Request request = chain.request()
//                .newBuilder()
////                .addHeader("Cookie", "JSESSIONID="+(sessionId!=null?sessionId:""))
//                .addHeader("Cookie", (sessionId != null ? sessionId : ""))
//                .build();
        logRequest(request);

        Response response = getCookie(chain, request);//TODO 添加--获取cookie

//        request = setCookie(request);//TODO 添加--保存cookie
//        Response response = chain.proceed(request);
        return logResponse(response);
    }

    private void logRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            XLog.d(TAG, "url : " + url);
            XLog.d(TAG, "method : " + request.method());
            if (headers != null && headers.size() > 0) {
                XLog.d(TAG, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        XLog.d(TAG, "params : " + bodyToString(request));
                    } else {
                        XLog.d(TAG, "params : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response logResponse(Response response) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        String resp = body.string();
                        XLog.json(Log.DEBUG, TAG, resp);

                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else {
                        XLog.d(TAG, "data : " + " maybe [file part] , too large too print , ignored!");
                    }
                } else {
                    //打印返回数据
                    String resp = body.string();
                    XLog.d(TAG, "ResponseBody : " + resp);
                    body = ResponseBody.create(mediaType, resp);
                    return response.newBuilder().body(body).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType == null) return false;

        return ("text".equals(mediaType.subtype())
                || "json".equals(mediaType.subtype())
                || "xml".equals(mediaType.subtype())
                || "html".equals(mediaType.subtype())
                || "webviewhtml".equals(mediaType.subtype())
                || "x-www-form-urlencoded".equals(mediaType.subtype()));
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final Exception e) {
            return "something error when show requestBody.";
        }
    }

    private Response getCookie(Chain chain, Request request) throws IOException {
        // 获取 Cookie
        Response resp = chain.proceed(request);
        List<String> setCookies = resp.headers("Set-Cookie");
        String cookieStr = "";
        if (setCookies != null && setCookies.size() > 0) {
            String s = setCookies.get(0);
            //sessionid值格式：JSESSIONID=AD5F5C9EEB16C71EC3725DBF209F6178，是键值对，不是单指值
            cookieStr = s.substring(0, s.indexOf(";"));
            //持久化到本地
//            SystemParams.getInstance().setString("COOKIES", cookieStr);
            XLog.e("COOKIES保存", cookieStr);
//            String sessionId = SPutils.getInstance().getStringFromSp(MyApplication.getInstance(), "SessionId");
//            if ("".equals(sessionId) || TextUtils.isEmpty(sessionId)) {
//                SPutils.getInstance().putStringIntoSp(MyApplication.getInstance(), "SessionId", cookieStr);
//                XLog.e("COOKIES保存2", cookieStr);
//            }
        }
        return resp;
    }

//    private Request setCookie(Request request) {
//        //设置cookie
//        String cookieStr = SystemParams.getInstance().getString("COOKIES", "");
//        XLog.e("COOKIES读取", cookieStr);
//        //判断cookie不为空
//        if (!cookieStr.isEmpty()) {
//            Request cookieRequest = request.newBuilder().addHeader("Cookie", cookieStr).build();
//            return cookieRequest;
//        }
//        return request;
//    }
}
