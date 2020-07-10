package com.example.jun.base;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.jun.base.utils.SPutils;
import com.example.jun.base.utils.ToastUtils;
import com.hsjskj.hy.library_net.net.IModel;
import com.hsjskj.hy.library_net.net.NetError;
import com.hsjskj.hy.library_net.net.XApi;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Flowable;

/**
 * @author Wen xiao
 * @time 2020/3/17
 */
public class MainBaseFragment extends RxFragment {
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void setStatusBarShow(View top_view) {
        //这里需要手动设置一个view给状态栏
        if (null != top_view) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                int statusBarHeight1 = -1;
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");//获取状态栏高度
                if (resourceId > 0) {
                    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
                }
                ViewGroup.LayoutParams params = top_view.getLayoutParams();
                params.height = statusBarHeight1;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                top_view.setLayoutParams(params);
            }
        }
    }

    public void showDError(NetError error) {
        if (error != null) {
            switch (error.getType()) {
                case NetError.RepeatLogin:
                    ToastUtils.showToast("" + error.getMessage());
                    SPutils.getInstance().cleadUserData(getContext());//清除用户数据
//                    LoginActivity.start(getContext());
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
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
//        return f.compose(XApi.getApiTransformer())
//                .compose(XApi.getScheduler())
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY));
//    }

    protected boolean stringIsEmpty(String str) {
        return str == null || "".equals(str) || TextUtils.isEmpty(str);
    }
}
