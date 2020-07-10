package com.example.jun.baseproject

import android.util.Log
import androidx.lifecycle.*


/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */

class InfoViewModel(var infoRepository: InfoRepository) : ViewModel(), LifecycleObserver {
    companion object {
        var userInfoLiveData = MutableLiveData<UserData>()
    }

    fun callInfo(): LiveData<UserData> {
        infoRepository.loadInfo(object : OnTaskFinish {
            override fun onFinish(data: UserData) {
                userInfoLiveData.postValue(data)
            }
        })
        return userInfoLiveData
    }

    fun getInfo(): LiveData<UserData> {
        return userInfoLiveData
    }

    fun setInfo(userData: UserData) {
        userInfoLiveData.value = userData
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun infoResume() {
        Log.e("InfoViewModel", "ON_RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun infoPause() {
        Log.e("InfoViewModel", "ON_PAUSE")
    }
}