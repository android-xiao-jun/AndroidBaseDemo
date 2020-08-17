package com.example.data_binding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.jun.baseproject.R
import com.example.jun.baseproject.databinding.DataBindingTestViewBinding

/**
 * @author Jun
 * @Description BaseProject
 * @Time 2020/8/17
 */
class ActivityDataBindingActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<DataBindingTestViewBinding>(this, R.layout.data_binding_test_view)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DataBindingModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = viewModel;
        binding.lifecycleOwner = this

        viewModel.getDataList();//加载数据模拟
    }

}