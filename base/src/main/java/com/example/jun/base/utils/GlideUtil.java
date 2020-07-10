package com.example.jun.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Created by lnliy on 2018/7/25.
 */

public class GlideUtil {

    static GlideUtil instance;
    RequestOptions options = new RequestOptions()
//            .placeholder(R.drawable.logo).error(R.drawable.logo)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    public static GlideUtil getInstance() {
        if (instance == null) instance = new GlideUtil();
        return instance;
    }

    public void setPicUserImageDefault(Context context, String url, ImageView imageView) {
        RequestOptions options2 = new RequestOptions()
//                .placeholder(R.drawable.default_head).error(R.drawable.default_head)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(url).apply(options2).into(imageView);

    }

    public void setPic(Context context, Bitmap bitmap, ImageView imageView) {

        Glide.with(context).load(bitmap).apply(options).into(imageView);

    }

    RequestOptions optionsGif = new RequestOptions()
//            .placeholder(R.drawable.logo).error(R.drawable.logo)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    public void setPicGif(Context context, Object object, ImageView imageView) {

        Glide.with(context).asGif().load(object).apply(optionsGif).into(imageView);

    }

    public void setPic(Context context, Drawable drawable, ImageView imageView) {

        Glide.with(context).load(drawable).apply(options).into(imageView);

    }



    public void setPic(Context context, String string, ImageView imageView) {

        Glide.with(context).load(string).apply(options).into(imageView);

    }
    public void setPic(Context context, String string, ImageView imageView,int res) {
//,R.drawable.signature_manager
        RequestOptions options = new RequestOptions()
                .placeholder(res).error(res)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(string).apply(options).into(imageView);

    }


    public void setPic(Context context, Uri uri, ImageView imageView) {

        Glide.with(context).load(uri).apply(options).into(imageView);

    }

    public void setPicCode(Context context, String string, ImageView imageView) {

        Glide.with(context).load(string).apply(options).into(imageView);

    }

    public void setPic(Context context, File file, ImageView imageView) {

        Glide.with(context).load(file).apply(options).into(imageView);

    }


    public void setPic(Context context, Object obj, ImageView imageView) {

        Glide.with(context).load(obj).apply(options).into(imageView);
    }

    public Bitmap getPicBit(Context context, String string) {

        return null;
    }

    //base64
    public  void loadImageForBase64(Context context, String url,  ImageView target){
        byte[] decode = null;
//        if(isBase64Img(url))
//        {
//            url = url.split(",")[1];
            decode = Base64.decode(url, Base64.DEFAULT);
//        }

        Glide.with(context).load(decode==null ?url:decode).into(target);
    }


}
