package com.hsjskj.hy.library_net.net;

/**
 * Created by wanglei on 2016/12/24.
 */

public class NetError extends Exception {
    public static final int ParseError = 0;   //数据解析异常
    public static final int NoConnectError = 1;   //无连接异常
    public static final int AuthError = 2;   //用户验证异常
    public static final int NoDataError = 3;   //无数据返回异常
    public static final int BusinessError = 4;   //业务异常
    public static final int OtherError = 5;   //其他异常
    public static final int RepeatLogin = 6; //重复登录
    private Throwable exception;
    private int type = NoConnectError;
    private String code = "";

    public String getCode() {
        return code;
    }

    public NetError(Throwable exception, int type) {
        this.exception = exception;
        this.type = type;
    }

    public NetError(String detailMessage, int type) {
        super(detailMessage);
        this.type = type;
    }

    public NetError(String detailMessage, String code, int type) {
        super(detailMessage);
        this.type = type;
        this.code = code;
    }

    @Override
    public String getMessage() {
        if (exception != null) return exception.getMessage();
        return super.getMessage();
    }

    public int getType() {
        return type;
    }
}
