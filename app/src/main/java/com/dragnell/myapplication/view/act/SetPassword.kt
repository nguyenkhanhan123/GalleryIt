package com.dragnell.myapplication.view.act

import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.dragnell.myapplication.CommonUtils
import com.dragnell.myapplication.R
import com.dragnell.myapplication.databinding.SetPasswordBinding
import com.dragnell.myapplication.view.PatternListener
import com.dragnell.myapplication.viewmodel.CommonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SetPassword : BaseActivity<SetPasswordBinding, CommonViewModel>() {

    private var isPattern: Boolean = false

    private val password = mutableListOf<Int>()

    private lateinit var dotViews: List<ImageView>

    private var currentInputCount = 0

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        dotViews = listOf(mbinding.dot1, mbinding.dot2, mbinding.dot3, mbinding.dot4)

        mbinding.lnSwitch.setOnClickListener {
            if (!isPattern) {
                isPattern = true
                mbinding.tvTypePassword.text = this.getString(R.string.set_pattern)
                mbinding.text.text = this.getString(R.string.draw_pattern)
                mbinding.lnPin.visibility = View.GONE
                mbinding.glNumber.visibility = View.GONE
                mbinding.customPatternView.visibility = View.VISIBLE

            } else {
                isPattern = false
                mbinding.tvTypePassword.text = this.getString(R.string.set_pin)
                mbinding.text.text = this.getString(R.string.set_pin)
                mbinding.lnPin.visibility = View.VISIBLE
                mbinding.glNumber.visibility = View.VISIBLE
                mbinding.customPatternView.visibility = View.GONE
            }
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
                savePassword("Pattern", selectedPoints.toString())
            }
        }
    }

    private fun savePassword(type: String, password: String) {
        CommonUtils.getInstance().savePref("Type", type)
        CommonUtils.getInstance().savePref("Password", password)
        CoroutineScope(Dispatchers.Main).launch{
            delay(500)
            startActivity(Intent(this@SetPassword,LockerAct::class.java))
            finish()
        }
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
            savePassword("PIN", password.toString())
        }
    }

    override fun initViewBinding(): SetPasswordBinding {
        return SetPasswordBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = SetPassword::class.java.name
    }
}