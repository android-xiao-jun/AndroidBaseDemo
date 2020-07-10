package com.example.jun.base.weight;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;


public class RefreshDialog {
    private Context mContext;
    private WaitingDialog dialog;

    DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode,
                             KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_HOME
                    || keyCode == KeyEvent.KEYCODE_SEARCH) {
                return false;
            }
            return false;
        }
    };

    private static RefreshDialog instance;

    public static RefreshDialog getInstance() {
        if (instance == null) {
            synchronized (RefreshDialog.class) {
                if (instance == null) {
                    instance = new RefreshDialog();
                }
            }
        }
        return instance;
    }

    public RefreshDialog showProcessDialog(Context context) {
        mContext = null;
        mContext = context;

        if (dialog == null) {
            try {
                dialog = new WaitingDialog(mContext);
                dialog.setOnKeyListener(keyListener);
                dialog.show();
            } catch (Exception e) {
            }

        } else if (dialog.getContext() != mContext) {
            cancleShow();
            dialog = new WaitingDialog(context);
            dialog.setOnKeyListener(keyListener);
            dialog.show();
        }

        return instance;

    }

    public RefreshDialog setMessage(String message) {
        if (null != dialog) {
            dialog.setText(message);
        }
        return instance;
    }

    public void cancleShow() {
        mContext = null;

        if (null == dialog) {
            return;
        }
        if (dialog.isShowing()) {
            try {
                dialog.cancel();
                dialog = null;
            } catch (Exception e) {
            }
        }else {
            dialog = null;
        }
    }

    public void cancleShow(String message) {

        if (null == dialog) {
            return;
        }
        if (dialog.isShowing()) {
            try {
                dialog.setText(message);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog.cancel();
                            dialog = null;
                        }
                    }
                }, 800);
            } catch (Exception e) {
            }

        }
        mContext = null;
    }

}
