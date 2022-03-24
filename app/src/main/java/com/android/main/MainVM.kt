package com.android.main

import com.llj.baselib.IOTViewModel

class MainVM:IOTViewModel() {

    private var order = ""

    fun sendOrder(order:String){
        this.order = order
        sendOrderToDevice(order)
    }

    fun turnOffLight(){
        val o = when(order){
            "A"->"B"
            "C" -> "D"
            "E" -> "F"
            else -> ""
        }
        if (o.isEmpty()) return
        sendOrderToDevice(o)
    }

}