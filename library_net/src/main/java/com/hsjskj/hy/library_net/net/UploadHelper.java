package com.hsjskj.hy.library_net.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * author        : Jun
 * time          : 2019年6月12日
 * description   : AndroidSheep retrofit2 文件上传
 */
public class UploadHelper {
    private volatile static UploadHelper mInstance;
    public static Map<String, RequestBody> params;

    private UploadHelper() {
    }

    //单例模式
    public static UploadHelper getInstance() {
        if (mInstance == null) {
            synchronized (UploadHelper.class) {
                if (mInstance == null)
                    mInstance = new UploadHelper();
                params = new HashMap<>();
            }
        }
        return mInstance;
    }

    //根据传进来的Object对象来判断是String还是File类型的参数
    public UploadHelper addParameter(String key, Object o) {
        if (o instanceof String) {
//            RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), (String) o);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (String) o);
            params.put(key, body);
        } else if (o instanceof File) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), (File) o);
//            params.put(key + "\"; filename=\"" + ((File) o).getName() + "", body);//有的版本 中文 传不上去图片
            String name = ((File) o).getName();
            //获取文件的后缀名 .jpg
            String suffix = name.substring(name.lastIndexOf("."));
            params.put(key + "\"; filename=\"" + System.currentTimeMillis() + suffix + "", body);
        }
        return this;
    }

    //建造者模式
    public Map<String, RequestBody> builder() {
        return params;
    }

    //清除参数
    public void clear() {
        params.clear();
    }


}
