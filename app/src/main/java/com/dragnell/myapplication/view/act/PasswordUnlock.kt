package com.dragnell.myapplication.view.act

import com.dragnell.myapplication.databinding.PasswordLockBinding
import com.dragnell.myapplication.viewmodel.CommonViewModel

class PasswordUnlock : BaseActivity<PasswordLockBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {

    }

    override fun initViewBinding(): PasswordLockBinding {
        return PasswordLockBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = PasswordUnlock::class.java.name
    }
}