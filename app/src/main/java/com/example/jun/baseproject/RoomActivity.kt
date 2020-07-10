package com.example.jun.baseproject

import android.os.Bundle
import android.util.Log
import com.example.jun.base.BaseActivity
import com.example.jun.base.weight.TitleBar
import com.example.room_lib.User
import com.example.room_lib.Utils
import kotlinx.android.synthetic.main.activity_room.*

/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */
class RoomActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_room
    }

    override fun buildTitle(bar: TitleBar): Boolean {
        bar.setTitleText("room")
        return true
    }

    override fun addListener() {
        button1.setOnClickListener {
            Utils.insert(this)
        }
        button2.setOnClickListener {
            Utils.deleteAllUsers(this)
        }
        button3.setOnClickListener {
            val list: Array<User>? = Utils.getUserById(this)
            for (user in list!!) {
                Log.e("所有用户","id:${user.id},userName:${user.userName}")
            }
        }
    }

    override fun initView() {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

}