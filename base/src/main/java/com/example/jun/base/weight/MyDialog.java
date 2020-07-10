package com.example.jun.base.weight;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/8/28.
 */

public class MyDialog extends Dialog {
    //    style引用style样式
    public MyDialog(Context context, View layout, int style, int location) {

        super(context, style);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = location;
        if (location == Gravity.BOTTOM) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        window.setAttributes(params);
        window.setWindowAnimations(style);
    }

    public MyDialog(Context context, View layout, int style, int location, int width, int height) {

        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = location;
        params.width = width;
        params.height = height;
        window.setAttributes(params);
    }
}
