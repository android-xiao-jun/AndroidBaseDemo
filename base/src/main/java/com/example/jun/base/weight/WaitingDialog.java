package com.example.jun.base.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jun.base.R;
import com.hsjskj.hy.library_net.BuildConfig;


/**
 * author        : Jun
 * time          : 2019年11月27日
 * description   : MK
 */
public class WaitingDialog extends Dialog {

    private TextView textView;
    private Animation animation;

    public WaitingDialog(Context context) {
//            this(context, R.style.RefreshDialog);
        this(context, R.style.NoBackGroundDialog);
    }

    public WaitingDialog(Context context, int themeResId) {
//            super(context, R.style.RefreshDialog);
        super(context, R.style.NoBackGroundDialog);
    }

    public WaitingDialog setText(String text) {
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_refresh);
        imageView = (ImageView) findViewById(R.id.img);
        textView = (TextView) findViewById(R.id.tbv);

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
//                animation.setInterpolator(new LinearInterpolator());
        if (animation != null) {
            imageView.clearAnimation();
            imageView.setAnimation(animation);
            imageView.startAnimation(animation);
        }

        setCancelable(BuildConfig.DEBUG);
        setCanceledOnTouchOutside(BuildConfig.DEBUG);

    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        if (animation != null) {
            animation.cancel();
            animation=null;
        }
        if (imageView != null) {
            imageView.clearAnimation();
            imageView=null;
        }
        textView=null;
    }

    @Override
    public void setOnCancelListener( OnCancelListener listener) {
        super.setOnCancelListener(listener);
        if (animation != null) {
            animation.cancel();
            animation=null;
        }
        if (imageView != null) {
            imageView.clearAnimation();
            imageView=null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animation != null) {
            animation.cancel();
            animation=null;
        }
        if (imageView != null) {
            imageView.clearAnimation();
            imageView=null;
        }
    }
}
