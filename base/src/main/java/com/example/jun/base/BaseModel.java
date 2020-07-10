package com.example.jun.base;

import com.hsjskj.hy.library_net.net.IModel;

import java.io.Serializable;

/**
 * @author Wen xiao
 * @time 2020/3/21
 */
public class BaseModel implements IModel, Serializable {
    //新的
    public boolean success;   //判断 是否成功
    public String message;   //请重新登录 提示的时候
    public String code;

    protected boolean error;

    public boolean isError() {
        return error;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isAuthError() {
        return !success;
//        if (code != null && code.equals("200"))
//            return false;
//        if (code != null && code.equals("9999")) {
//            return false;
//        }
//        return true;
    }

    @Override
    public boolean isBizError() {
        return error;
    }

    @Override
    public boolean isRepeatLogin() {
        if (code != null && code.equals("401")) {
            return true;
        }else if (code != null && code.equals("403")) {
            return true;//403 账号锁定--退出登录
        }
        return false;
    }

    @Override
    public String getErrorMsg() {
        return message;
    }
}