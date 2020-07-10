package com.hsjskj.hy.library_net.net.converter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2017-11-07.
 */

public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    @Override
    public RequestBody convert(T value) throws IOException {

        byte[] bytes = JSON.toJSONBytes(value);

//        String str = (String) JSON.toJSONString(value);

        return RequestBody.create(MEDIA_TYPE, bytes);
    }
}
