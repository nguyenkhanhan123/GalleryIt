package com.dragnell.myapplication.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    companion object {
        val TAG: String = BaseViewModel::class.java.name
    }

}
