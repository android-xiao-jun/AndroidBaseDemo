package com.example.jun.baseproject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */

class InfoRepository {
    fun loadInfo(task: OnTaskFinish) {
        Executors.newSingleThreadExecutor().submit {
            val userData = UserData()
            userData.userName = "修改${System.currentTimeMillis()}"
            userData.userAge = 30
            Thread.sleep(1000)
            task.onFinish(userData)
        }

    }

    suspend fun loadInfo() = withContext(Dispatchers.IO) {
        val userData = UserData()
        userData.userName = "修改${System.currentTimeMillis()}"
        userData.userAge = 30
        Thread.sleep(1000)
        userData
    }
}

interface OnTaskFinish {
    fun onFinish(data: UserData)
}