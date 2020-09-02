package com.dt.sign;

/**
 * @author Jun
 * @Description BaseProject
 * @Time 2020/8/31
 */
public class SignUtils {
    static {
        System.loadLibrary("sign-jni");
    }

    public static native String getPublicKey();

    public static native String getPrivateKey();

}
