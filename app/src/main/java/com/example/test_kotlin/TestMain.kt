package com.example.test_kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.jun.baseproject.NextPageActivity
import java.lang.StringBuilder

/**
 *
 * @author Wen xiao
 * @time 2020/7/11
 */
fun main() {
//    var testA="TestAdsss"
//    println("Char_${getNameLastChar(testA)}")

//    toast("默认时间")
//    toast("指定时间",time = 3000)

//    testVararg("ssss", "", "2222", "", "33333")

//    var list=listOf("11","222","4444","555");//["11","222","4444","555"]
//    for (s in list) {
//        println("list_item：$s")
//    }
//    for (index in list.indices){
//        println("list_item：${list[index]}")
//    }

//    for (index in 0..3){
//        println("list_item：$index")
//    }

//    var i=0
//    while (i<10){
//        println("list_item：$i")
//        i++
//    }

//    for (index in IntRange(0,10)) {

//    for (index in 0..10) {
//        if (index%2!=0) continue
//        println("list_item：$index")
//    }

//    Button(true).click()
//
//    Button("登录按钮").click()
//
//    println("Button(true)_isEnable：${Button(true).isEnable}")

//   var point= Point(x = 10,y = 3)

//    var list = Day.values()
//    var day = Day.valueOf("SUNDAY")
//    println("day.index_${day.index}")
//    println("day.name_${day.name}")

//    val view :ViewClick=ViewClick()
//    view.setOnClickListener(object : OnClickListener {
//        override fun click() {
//            println("click")
//        }
//    })

//    Outer().Inner().foo1()
//    Outer.Nested().foo1()

    var but: Button = Button("登录按钮")
//    but.start(null)
//    val javaClass = but.javaClass
//    val fields =javaClass.declaredFields
//    fields.forEach {
//        it.isAccessible=true
//        println("Button_变量_name_${it.name},${it.get(but).toString()}")
//    }

//    val declaredMethods = javaClass.declaredMethods
//
//    declaredMethods.forEach {
//        it.isAccessible=true
//        println("Button_方法_name_${it.name}")
//
//    }

//    println("setEnable反射前")
//    println("but.isEnable_${but.isEnable}")
//    val method2 = Button::class.java.getDeclaredMethod("setEnable",Boolean::class.java)
//    method2.isAccessible=true
//    method2.invoke(but,false)
//    println("setEnable反射后")
//    println("but.isEnable_${but.isEnable}")
//
//    val method = Button::class.java.getDeclaredMethod("isEnable")
//    method.isAccessible=true
//    val isEnable = method.invoke(but)
//    println("isEnable反射查询_$isEnable")


//    val sum = funSum()
//    println("sum${sum(10)}")
//    println("sum${sum(10)}")
//    println("sum${sum(10)}")

    val list = listOf(1, 3, 9, 7, 9)
    println(list.max())
}

interface OnClickListener {
    fun click()
}

class ViewClick {
    fun setOnClickListener(onClickListener: OnClickListener) {
        println("setOnClickListener")
    }
}


class Outer {

    private val bar = 1

    class Nested {
        fun foo1() = 2
    }

    inner class Inner {
        fun foo1() = 2
        fun foo2() = bar
    }
}

fun getNameLastChar(str: String): Char = str[str.length - 1]


//指定默认值
fun toast(str: String, time: Int = 1500) {
    println("这是一个提示内容：$str,提示时间$time")
}

//检查空值
fun checkNull(str: String): String = if (str.isEmpty()) "_" else str

//可变参数
fun testVararg(vararg str: String) {

    println(with(StringBuilder()) {
        str.map { checkNull(it) }.forEach {
            this.append(it)
        }
        this.toString()
    })
}


enum class Day(val index: Int) {
    SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6)
}


class Button(private var b: Boolean) : View() {
    override fun click(): Boolean {
        if (b) {
            println("Button:click")
            return true
        }
        return super.click()
    }

    constructor(txet: String) : this(true) {
        println("构造函数$txet")
    }

    var isEnable: Boolean
        get() = b
        set(value) {
            b = value
        }

    companion object {


    }
}

fun funSum(): (Int) -> Int {
    var baseInt=0;
    return fun(s: Int): Int{
        baseInt+=s
        return baseInt
    }
}

//扩展函数
fun Button.start(context: Context?): Unit {
    var intent = Intent(context, NextPageActivity::class.java)
    var bundle = Bundle()
    bundle.putString("name", "")
    intent.putExtras(bundle)
    context?.startActivity(intent)
}


open class View {
    open fun click(): Boolean {
        println("View:click")
        return false
    }
}


data class Point(val x: Int, val y: Int)