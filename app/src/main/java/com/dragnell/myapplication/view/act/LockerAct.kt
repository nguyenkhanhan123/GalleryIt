package com.dragnell.myapplication.view.act

import com.dragnell.myapplication.databinding.LockerActBinding
import com.dragnell.myapplication.viewmodel.CommonViewModel

class LockerAct : BaseActivity<LockerActBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {

    }

    override fun initViewBinding(): LockerActBinding {
        return LockerActBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = LockerAct::class.java.name
    }
}