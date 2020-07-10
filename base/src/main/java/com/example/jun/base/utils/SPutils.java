package com.example.jun.base.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author Wen xiao
 * @time 2020/3/21
 */
public class SPutils {
    public String SP_Name = "hy";  //  sp 文件名

    public String KEY_Token = "token";  // token
    public String KEY_PHONE = "phone";
    public String KEY_NICKNAME = "nick";
    public String KEY_Email = "email";
    public String KEY_HeadImg = "head";
    public String KEY_role_int = "role";
    public String KEY_consignorApprove = "consignorApprove";
    public String KEY_carrierApprove = "carrierApprove";
    public String KEY_driverApprove = "driverApprove";
    public String KEY_realName = "realName";
    public String KEY_faceAuth = "faceAuth";

    public String KEY_OpenGrabSwitch = "grabswitch";
    public String KEY_ADDRESS_LIST = "address_list";//发货地址列表--之前添加的

    static SPutils instance;

    public static SPutils getInstance() {
        if (instance == null) instance = new SPutils();
        return instance;
    }

    public void putStringIntoSp(Context context, String key, String str) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        sp.edit().putString(key, str).commit();
    }

    public String getStringFromSp(Context context, String key) {
        return getStringFromSp(context, key, "");
    }

    public String getStringFromSp(Context context, String key, String defValue) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);

    }


    public void putBooleanIntoSp(Context context, String key, boolean b) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, b).commit();

    }

    public boolean getBooleanFromSp(Context context, String key) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);

        return sp.getBoolean(key, false);

    }


    public void putIntIntoSp(Context context, String key, Integer integer) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        sp.edit().putInt(key, integer).commit();
    }

    public Integer getIntFromSp(Context context, String key) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);

    }


    public void cleadUserData(Context context) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        //全部清空
        clearAllCache(context);
    }


    public void clearAllCache(Context context) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }


    public void deleteData(Context context, String key) {
        context = checkContext(context);
        SharedPreferences sp = context.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }


    private Context checkContext(Context context) {

        return context;
    }

}
