package com.example.jun.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Wen xiao
 * @time 2020/3/17
 */
public abstract class LayzFragment extends MainBaseFragment {
    protected boolean isViewCreated;//视图是否已经创建
    protected boolean isUiVisible;//该fragment是否对用户可见

    protected View view;
    public Unbinder unbinder;
    public Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        isViewCreated = true;
        lazyLoad();
        return view;
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,
        // 并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUiVisible) {
            lazyLoadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUiVisible = false;

        }
    }

    public abstract void lazyLoadData();

    public abstract int getLayoutId();


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUiVisible = true;
            lazyLoad();
        } else {
            isUiVisible = false;
        }
    }

    //FragmentPagerAdapter 使用 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT之后，，setUserVisibleHint没有效果
    //Lifecycle.State.RESUMED 同 setUserVisibleHint(true)
//    @Override
//    public void onResume() {
//        super.onResume();
//        isUiVisible = true;
//        lazyLoad();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        isViewCreated = false;
        isUiVisible = false;
    }
}
