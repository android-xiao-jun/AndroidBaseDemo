package com.example.jun.baseproject

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jun.base.BaseActivity
import com.example.jun.base.utils.ToastUtils
import com.example.jun.base.weight.TitleBar
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */
class ThreeActivity : BaseActivity() {
    private lateinit var infoViewModel: InfoViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun buildTitle(bar: TitleBar): Boolean {
        bar.apply {
            setTitleText("第二页")
//            hideLeft()
            setRightText("右侧按钮")
            setRightClick {
                ToastUtils.showSingleLongToast("这是个提示")
            }
        }
        return true
    }

    override fun initView() {
        get_info.setOnClickListener {
            infoViewModel.getInfo().observe(this, Observer {
                ToastUtils.showLongToast("value${it.userName}")
            })
        }
        next_page.setOnClickListener {
            infoViewModel.setInfo(UserData().apply {
                userName = "haha"
                userAge = 31
            })
        }
    }

    override fun addListener() {
        infoViewModel = ViewModelProvider(this, InfoFactory(InfoRepository())).get(InfoViewModel::class.java)
        lifecycle.addObserver(infoViewModel)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

}

