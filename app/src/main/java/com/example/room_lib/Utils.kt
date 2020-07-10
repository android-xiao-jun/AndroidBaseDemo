package com.example.room_lib

import android.content.Context

/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */
object Utils {

    fun insert(context: Context){
        UsersDatabase.getInstance(context).userDao().insertUser(User(userName = "哈哈哈${System.currentTimeMillis()}"))
    }

    fun deleteAllUsers(context: Context){
        UsersDatabase.getInstance(context).userDao().deleteAllUsers()
    }

    fun getUserById(context: Context):Array<User>?{
      return  UsersDatabase.getInstance(context).userDao().getUserById()
    }

}