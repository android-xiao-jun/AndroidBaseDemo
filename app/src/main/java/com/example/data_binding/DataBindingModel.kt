package com.example.data_binding

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jun.base.handler.HandlerUtil
import com.example.jun.base.utils.ToastUtils

/**
 * @author Jun
 * @Description BaseProject
 * @Time 2020/8/17
 */
class DataBindingModel : ViewModel() {
    val textTest = MutableLiveData<String>();
    val data = MutableLiveData<DataBean>();

    var isLoading = MutableLiveData<Boolean>();

    fun getDataList() {
        isLoading.value = true;
        data.value = DataBean(name = "加载中", des = "描述：默认数据");
        HandlerUtil.getInstance().workHandler.post {
            Thread.sleep(2000)
            HandlerUtil.getInstance().mainHandler.post {
                isLoading.value = false;
                data.value = DataBean(name = "这是一个标题", des = "描述：延迟加载");
            }
        }
    }

    fun onClickButton(v: View) {
        isLoading.value = true;
        HandlerUtil.getInstance().workHandler.post {
            Thread.sleep(2000)
            HandlerUtil.getInstance().mainHandler.post {
                isLoading.value = false
                ToastUtils.showSingleToast("加载结束")
            }
        }
    }
}