package com.example.jun.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.jun.base.utils.SPutils;
import com.example.jun.base.utils.ToastUtils;
import com.example.jun.base.weight.TitleBar;
import com.gyf.immersionbar.ImmersionBar;
import com.hsjskj.hy.library_net.net.IModel;
import com.hsjskj.hy.library_net.net.NetError;
import com.hsjskj.hy.library_net.net.XApi;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends RxAppCompatActivity {
//    public static NetBroadcastReceiver.NetChangeListener listener;
    /**
     * 网络类型
     */
    private int netType;
//    private NetBroadcastReceiver netBroadcastReceiver;

    public CompositeDisposable compositeDisposable;
    protected ViewGroup mRootView;
    protected LinearLayout mContentRootView;
    protected TitleBar mTitleBar;
    protected BaseActivity mContext;
    private RxPermissions rxPermissions;
    private Unbinder mUnBinder;
    protected int statusBarHeight1;
    //    private MyApplication application;


//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        if("FrameLayout".equals(name)){
//            int count = attrs.getAttributeCount();
//            for (int i = 0; i < count; i++) {
//                String attributeName = attrs.getAttributeName(i);
//                String attributeValue = attrs.getAttributeValue(i);
//                if (attributeName.equals("id")) {
//                    int id = Integer.parseInt(attributeValue.substring(1));
//                    String idVal = getResources().getResourceName(id);
//                    if ("android:id/content".equals(idVal)) {
//                        return new GrayFrameLayout(context, attrs);
//                    }
//                }
//            }
//        }
//        return super.onCreateView(name, context, attrs);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止启动页多次打开
        if (!isTaskRoot()) {
            Intent i = getIntent();
            String action = i.getAction();
            if (i.hasCategory(Intent.CATEGORY_APP_CALENDAR) && !TextUtils.isEmpty(action) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }

        setContentView(R.layout.base_activity);
        mContext = this;
        compositeDisposable = new CompositeDisposable();
        ImmersionBar.with(this).init();
        basicInitialize();
        mUnBinder = ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            statusBarHeight1 = -1;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");//获取状态栏高度
            if (resourceId > 0) {
                statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
            }
            ViewGroup.LayoutParams params = mTitleBar.getTopView().getLayoutParams();
            params.height = statusBarHeight1;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mTitleBar.getTopView().setLayoutParams(params);
        }
        initData(savedInstanceState);
        initView();
        addListener();

//        initNetListener();
    }

    protected void setStatusBarHeight(View view) {
        if (view == null) return;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");//获取状态栏高度
        if (resourceId > 0) {
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }

    private void basicInitialize() {
        basicFindViews();
        basicSetting();
    }

    private void basicSetting() {
        addContent();
        buildTitle();
    }

    private void addContent() {
        final int layoutId = getLayoutId();
        if (layoutId <= 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(layoutId, mContentRootView, true);
    }

    private void buildTitle() {
        final TitleBar bar = mTitleBar;
        if (bar == null)
            return;
        bar.setActivity(this);
        if (!buildTitle(mTitleBar))
            mTitleBar.setVisibility(View.GONE);
        bar.enableLeftImageBackOnClick();
    }

    protected abstract int getLayoutId();

    protected abstract boolean buildTitle(TitleBar bar);


    private void basicFindViews() {
        mRootView = findViewById(R.id.rootView_baseActivity);
        mContentRootView = findViewById(R.id.content);
        mTitleBar = findViewById(R.id.titleBar);
    }

    protected abstract void addListener();

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (netBroadcastReceiver!=null){
//            unregisterReceiver(netBroadcastReceiver);
//        }

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        compositeDisposable.dispose();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //释放资源
        mRootView = null;
        mContentRootView = null;
        mTitleBar = null;
        mContext = null;
        rxPermissions = null;


//        if (LoadingDialog.mDialog != null && LoadingDialog.mDialog.isShowing()) {
//            LoadingDialog.mDialog.cancel();
//        }
    }

    public RxPermissions getRxPermissions() {
        rxPermissions = new RxPermissions(this);
//        rxPermissions.setLogging(XDroidConf.DEV); //不打印日志
        return rxPermissions;
    }


    public void showDError(NetError error) {
        if (error != null) {
            switch (error.getType()) {
                case NetError.RepeatLogin:
                    ToastUtils.showToast("" + error.getMessage());
                    SPutils.getInstance().cleadUserData(mContext);//清除用户数据
//                    LoginActivity.start(mContext);
                    finish();
                    break;

                case NetError.ParseError:
                    ToastUtils.showSingleLongToast(R.string.base_data_parsing_error);
                    break;

                case NetError.AuthError:
                    ToastUtils.showSingleLongToast("" + error.getMessage());
                    break;

                case NetError.BusinessError:
                    ToastUtils.showSingleLongToast(R.string.base_business_exceptions);
                    break;

                case NetError.NoConnectError:
                    ToastUtils.showSingleLongToast(R.string.base_network_connectionless);

//                    if (isStartNetError) {
//                        ErrorActivity.start(mContext);
//                        finish();
//                    }
                    break;

                case NetError.NoDataError:
                    ToastUtils.showSingleLongToast("" + error.getMessage());//数据为空
                    break;

                case NetError.OtherError:
                    ToastUtils.showSingleLongToast(getString(R.string.base_network_anomalies) + error.getMessage());

//                    if (isStartNetError) {
//                        ErrorActivity.start(mContext);
//                        finish();
//                    }
                    break;

            }
        }
    }

//    protected <T extends IModel> Flowable<T> flowableBase(Flowable<T> f) {
//        return f.compose(XApi.getApiTransformer()).compose(XApi.getScheduler()).compose(bindToLifecycle());
//    }

    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }


    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }


    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    protected void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    protected void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(editText, 0);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight() + statusBarHeight1,
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    protected boolean stringIsEmpty(String str) {
        return str == null || "".equals(str) || TextUtils.isEmpty(str);
    }

//    private void initNetListener() {
//        listener = this;
    //Android 7.0以上需要动态注册
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    //实例化IntentFilter对象
//            IntentFilter filter = new IntentFilter();
//            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//            netBroadcastReceiver = new NetBroadcastReceiver();
    //注册广播接收
//            registerReceiver(netBroadcastReceiver, filter);
//        }
//    }

//    @Override
//    public void onChangeListener(int status) {
//        this.netType = status;
//        if (!isNetConnect()) {
//            //网络异常，请检查网络---------防止没有网络的时候
//            if (LoadingDialog.mDialog != null) {
//                LoadingDialog.mDialog.cancel();
//            }
//        } else {
//            //网络恢复了
//        }
//    }
//
//    /**
//     * 判断有无网络 。
//     *
//     * @return true 有网, false 没有网络.
//     */
//    public boolean isNetConnect() {
//        if (netType == 1) {
//            return true;
//        } else if (netType == 0) {
//            return true;
//        } else if (netType == -1) {
//            return false;
//        }
//        return false;
//    }
}
