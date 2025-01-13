package com.dragnell.myapplication.view

import java.util.Objects

interface OnMainCallBack2 {
    fun showFragment(tag:String, data: Objects?, isBack:Boolean,viewID:Int)
}