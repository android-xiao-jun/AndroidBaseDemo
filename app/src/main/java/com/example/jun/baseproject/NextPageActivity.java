package com.example.jun.baseproject;

import android.os.Bundle;
import android.view.View;

import com.example.jun.base.BaseActivity;
import com.example.jun.base.utils.ToastUtils;
import com.example.jun.base.weight.TitleBar;

import androidx.lifecycle.ViewModelProvider;

/**
 * @author Wen xiao
 * @time 2020/7/9
 */
public class NextPageActivity extends BaseActivity {
    private  InfoViewModel infoViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean buildTitle(TitleBar bar) {
        bar.hideLeft();
        bar.setTitleText("第二页");
        return true;
    }

    @Override
    protected void addListener() {
        InfoRepository infoRepository =new InfoRepository();
        InfoFactory infoFactory = new InfoFactory(infoRepository);
        infoViewModel =new ViewModelProvider(this, infoFactory).get(InfoViewModel.class);
        getLifecycle().addObserver(infoViewModel);
    }

    @Override
    protected void initView() {
        View get_info = findViewById(R.id.get_info);
        View next_page = findViewById(R.id.next_page);
        get_info.setOnClickListener(v->{
           infoViewModel.getInfo().observe(this, userData -> {
               ToastUtils.showSingleToast("value"+userData.getUserName());
           });
        });

        next_page.setOnClickListener(v -> {
            UserData userData = new UserData();
            userData.userName = "haha";
            userData.setUserAge(31);
            infoViewModel.setInfo(userData);
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
