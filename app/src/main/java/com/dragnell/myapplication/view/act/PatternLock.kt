package com.dragnell.myapplication.view.act

import android.graphics.Color
import android.util.Log
import com.dragnell.myapplication.databinding.PatternLockBinding
import com.dragnell.myapplication.view.PatternListener
import com.dragnell.myapplication.viewmodel.CommonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PatternLock : BaseActivity<PatternLockBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        mbinding.customPatternView.patternListener  = object : PatternListener {
            override fun onPatternDetected(selectedPoints: List<Int>) {
                CoroutineScope(Dispatchers.Main).launch{
                    mbinding.customPatternView.updateColors(Color.GREEN)
                    delay(500)
                    Log.i("selectedPoints",selectedPoints.toString())
                    mbinding.customPatternView.resetPattern()
                }

            }
        }
    }

    override fun initViewBinding(): PatternLockBinding {
        return PatternLockBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = PatternLock::class.java.name
    }
}