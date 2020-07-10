package com.example.jun.base;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jun.base.utils.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wen xiao
 * @time 2020/3/17
 */
public abstract class BaseRefreshActivity<T> extends BaseActivity {
    protected SmartRefreshLayout swipeRefresh;
    protected RecyclerView recyclerView;
    protected ImageView img;
    protected TextView noDataTv;
    protected CommonAdapter adapterOld;
    protected HeaderAndFooterWrapper adapter;
    protected List<T> mList;

    protected int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.base_refresh_activity;
    }

    @Override
    protected void addListener() {
//        swipeRefresh.setEnableLoadMoreWhenContentNotFull(false);
        swipeRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                swipeLoadMore(refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                swipeRefresh(refreshLayout);
            }
        });
        adapterOld = new CommonAdapter<T>(this, getItemLayout(), mList) {
            @Override
            protected void convert(ViewHolder holder, T o, int position) {
                setLayoutItem(holder, o, position);
            }
        };
        adapter = new HeaderAndFooterWrapper(adapterOld);
        adapterOld.setOnItemClickListener(setOnItemClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置Item添加和移除的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //列表拖拽实现
//        ItemTouchHelper itemTouchHelp = new ItemTouchHelper(new RecycleItemTouchCallBack((f, t) -> {
//            RecycleItemTouchCallBack.move(f, t, adapter, mList);
//        }));
//        itemTouchHelp.attachToRecyclerView(recyclerView);
        getHttpData();
    }

    protected abstract void setLayoutItem(ViewHolder holder, T o, int position);

    protected abstract int getItemLayout();

    protected abstract void getHttpData();

    protected abstract MultiItemTypeAdapter.OnItemClickListener setOnItemClickListener();

    protected abstract void swipeLoadMore(RefreshLayout refreshLayout);

    protected abstract void swipeRefresh(RefreshLayout refreshLayout);

    //隐藏错误（没数据）布局
    protected void hideErrorView() {
        findViewById(R.id.noDataTvView).setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    //显示错误（没有数据）布局
    protected void showErrorView(String msg) {
        if (!"".equals(msg) && noDataTv != null) {
            noDataTv.setText(msg);
        }
        findViewById(R.id.noDataTvView).setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    //添加间隔线
    protected void addItemDecoration(float h) {
        recyclerView.addItemDecoration(new DividerItemDecoration(this, h, 0, Color.parseColor("#00000000")));
    }

    //显示错误布局的
    protected void showErrorView() {
        showErrorView(getResources().getString(R.string.no_data));
    }

    protected void setRecyclerStatus() {
        if (mList.isEmpty()) {
            showErrorView();
        } else {
            hideErrorView();
        }
    }

    protected void inishLoadRefresh() {
        if (swipeRefresh != null) {
            swipeRefresh.finishRefresh();
            swipeRefresh.finishLoadMore();
        }
    }

    @Override
    protected void initView() {
        swipeRefresh = (SmartRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recyclerView);
        img = (ImageView) findViewById(R.id.img);
        noDataTv = (TextView) findViewById(R.id.noDataTv);
        mList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        mList.clear();
        mList = null;
        noDataTv = null;
        img = null;
        recyclerView = null;
        swipeRefresh = null;
        adapter = null;
        super.onDestroy();
    }
}
