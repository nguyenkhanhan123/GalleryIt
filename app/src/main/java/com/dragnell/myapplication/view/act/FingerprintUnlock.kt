package com.dragnell.myapplication.view.act

import android.content.Intent
import com.dragnell.myapplication.databinding.FingerprintUnlockBinding
import com.dragnell.myapplication.viewmodel.CommonViewModel

class FingerprintUnlock : BaseActivity<FingerprintUnlockBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        mbinding.btOK.setOnClickListener {
            startActivity(Intent(this, SetPassword::class.java))
            finish()
        }
        mbinding.btCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }

    override fun initViewBinding(): FingerprintUnlockBinding {
        return FingerprintUnlockBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = FingerprintUnlock::class.java.name
    }
}