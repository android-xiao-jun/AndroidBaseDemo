package com.example.jun.baseproject;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.chan.ypatchcore.YPatch;
import com.dt.sign.SignUtils;
import com.example.data_binding.ActivityDataBindingActivity;
import com.example.jun.base.BaseActivity;
import com.example.jun.base.utils.CommonUtil;
import com.example.jun.base.utils.ToastUtils;
import com.example.jun.base.weight.RefreshDialog;
import com.example.jun.base.weight.TitleBar;

import java.io.File;

public class MainActivity extends BaseActivity {
    private volatile InfoViewModel infoViewModel;
    private volatile InfoFactory infoFactory;
    private volatile InfoRepository infoRepository;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean buildTitle(TitleBar bar) {
        bar.hideLeft();
        bar.setTitleText("首页");
        return true;
    }

    @Override
    protected void addListener() {
        infoRepository = new InfoRepository();
        infoFactory = new InfoFactory(infoRepository);
        infoViewModel = new ViewModelProvider(this, infoFactory).get(InfoViewModel.class);
        getLifecycle().addObserver(infoViewModel);
    }

    @Override
    protected void initView() {
        View get_info = findViewById(R.id.get_info);
        View next_page = findViewById(R.id.next_page);
        View room = findViewById(R.id.room);
        findViewById(R.id.btn_databing).setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityDataBindingActivity.class));
        });
        findViewById(R.id.setting_bar).setOnClickListener(v -> {
            Log.e("getPublicKey", SignUtils.getPublicKey());
            Log.e("getPrivateKey", SignUtils.getPrivateKey());
        });
        get_info.setOnClickListener(v -> {
            RefreshDialog.getInstance().showProcessDialog(this).setMessage("加载数据中..");
            infoViewModel.callInfo().observe(this, userData -> {
                RefreshDialog.getInstance().cancleShow();
                ToastUtils.showSingleToast("" + userData.getUserName());
            });
        });

        next_page.setOnClickListener(v -> {
//            startActivity(new Intent(this,NextPageActivity.class));
//            startActivity(new Intent(this,ThreeActivity.class));
            final File destApk = new File(Environment.getExternalStorageDirectory(), "dest.apk");
            final File patch = new File(Environment.getExternalStorageDirectory(), "PATCH.patch");
//            String str = Environment.getExternalStorageDirectory()+"/"+"old.apk";
            String str = getApplicationContext().getPackageResourcePath();
            try {
                YPatch.patch(str, destApk.getAbsolutePath(), patch.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (destApk.exists())
                CommonUtil.installApk(this, destApk.getAbsolutePath());

        });

        room.setOnClickListener(v -> startActivity(new Intent(this, RoomActivity.class)));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

}
