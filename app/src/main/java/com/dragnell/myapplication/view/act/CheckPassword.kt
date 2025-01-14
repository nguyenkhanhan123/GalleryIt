package com.dragnell.myapplication.view.act

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.dragnell.myapplication.CommonUtils
import com.dragnell.myapplication.R
import com.dragnell.myapplication.databinding.CheckPasswordBinding
import com.dragnell.myapplication.view.PatternListener
import com.dragnell.myapplication.viewmodel.CommonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckPassword : BaseActivity<CheckPasswordBinding, CommonViewModel>() {
    private val password = mutableListOf<Int>()

    private lateinit var dotViews: List<ImageView>

    private var currentInputCount = 0

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        dotViews = listOf(mbinding.dot1, mbinding.dot2, mbinding.dot3, mbinding.dot4)

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

        val numberButtons = listOf(
            mbinding.number1,
            mbinding.number2,
            mbinding.number3,
            mbinding.number4,
            mbinding.number5,
            mbinding.number6,
            mbinding.number7,
            mbinding.number8,
            mbinding.number9,
            mbinding.number0
        )

        numberButtons.forEach { button ->
            button.setOnClickListener { onNumberClick(button.tag.toString().toInt()) }
        }

        mbinding.delete.setOnClickListener {
            if (currentInputCount > 0) {
                password.removeAt(password.size - 1)
                currentInputCount--
                dotViews[currentInputCount].setImageResource(R.drawable.gray_dot)
            }
        }

        mbinding.customPatternView.patternListener = object : PatternListener {
            override fun onPatternDetected(selectedPoints: List<Int>) {
                if (checkPassword(selectedPoints.toString())) {
                    CoroutineScope(Dispatchers.Main).launch {
                        mbinding.customPatternView.updateColors(Color.GREEN)
                        delay(500)
                        startActivity(Intent(this@CheckPassword, LockerAct::class.java))
                        finish()
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        mbinding.customPatternView.updateColors(Color.RED)
                        delay(500)
                        mbinding.customPatternView.resetPattern()
                    }
                }
            }
        }
    }

    private fun checkPassword(password: String): Boolean {
        val s = CommonUtils.getInstance().getPref("Password").toString()
        return s == password
    }

    private fun onNumberClick(number: Int) {
        if (password.size < 4) {
            password.add(number)
            resetLnDot()
        }
    }

    private fun resetLnDot() {
        if (currentInputCount < dotViews.size) {
            dotViews[currentInputCount].setImageResource(R.drawable.white_dot)
            currentInputCount++
        }
        if (currentInputCount == 4) {
            if (checkPassword(password.toString())) {
                CoroutineScope(Dispatchers.Main).launch {
                    resetColorLnDot(R.drawable.green_dot)
                    delay(500)
                    startActivity(Intent(this@CheckPassword, LockerAct::class.java))
                    finish()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    resetColorLnDot(R.drawable.red_dot)
                    delay(500)
                    resetColorLnDot(R.drawable.gray_dot)
                    currentInputCount = 0
                    password.clear()
                }
            }
        }
    }

    private fun resetColorLnDot(i: Int) {
        for (stt in dotViews) {
            stt.setImageResource(i)
        }
    }

    override fun initViewBinding(): CheckPasswordBinding {
        return CheckPasswordBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = CheckPassword::class.java.name
    }
}