package com.dragnell.myapplication.view.act

import android.view.View
import com.dragnell.myapplication.CommonUtils
import com.dragnell.myapplication.R
import com.dragnell.myapplication.databinding.CheckPasswordBinding
import com.dragnell.myapplication.viewmodel.CommonViewModel

class CheckPassword : BaseActivity<CheckPasswordBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        val s = CommonUtils.getInstance().getPref("Type").toString()
        if (s == "PIN") {
            mbinding.text.text = this.getString(R.string.use_pin_or_fingerprint)
            mbinding.lnPin.visibility = View.VISIBLE
            mbinding.glNumber.visibility = View.VISIBLE
            mbinding.customPatternView.visibility = View.GONE
        } else if (s == "Pattern") {
            mbinding.text.text = this.getString(R.string.use_pattern_or_fingerprint)
            mbinding.lnPin.visibility = View.GONE
            mbinding.glNumber.visibility = View.GONE
            mbinding.customPatternView.visibility = View.VISIBLE
        }
    }

    override fun initViewBinding(): CheckPasswordBinding {
        return CheckPasswordBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = CheckPassword::class.java.name
    }
}