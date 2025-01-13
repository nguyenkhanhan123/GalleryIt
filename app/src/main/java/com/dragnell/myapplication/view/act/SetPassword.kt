package com.dragnell.myapplication.view.act

import android.view.View
import android.widget.ImageView
import com.dragnell.myapplication.R
import com.dragnell.myapplication.databinding.SetPasswordBinding
import com.dragnell.myapplication.viewmodel.CommonViewModel

class SetPassword : BaseActivity<SetPasswordBinding, CommonViewModel>() {

    private var i: Int = 1

    private val password = mutableListOf<Int>()

    private lateinit var dotViews: List<ImageView>

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        dotViews = listOf(mbinding.dot1, mbinding.dot2, mbinding.dot3, mbinding.dot4)

        mbinding.lnSwitch.setOnClickListener {
            if (i == 1) {
                i = 0
                mbinding.tvTypePassword.text = this.getString(R.string.set_pattern)
                mbinding.text.text = this.getString(R.string.draw_pattern)
                mbinding.lnPin.visibility = View.GONE
                mbinding.glNumber.visibility = View.GONE
                mbinding.customPatternView.visibility = View.VISIBLE

            } else {
                i = 1
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
    }

    private fun onNumberClick(number: Int) {
        if (password.size < 4) {
            password.add(number)
            updateDots()
        }
    }

    private fun updateDots() {
        for (stt in 0 until password.size) {
            dotViews[stt].setImageResource(R.drawable.white_dot)
        }
    }

    override fun initViewBinding(): SetPasswordBinding {
        return SetPasswordBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = SetPassword::class.java.name
    }
}