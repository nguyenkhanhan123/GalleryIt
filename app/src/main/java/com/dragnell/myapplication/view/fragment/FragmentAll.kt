package com.dragnell.myapplication.view.fragment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import com.dragnell.myapplication.CommonUtils
import com.dragnell.myapplication.ManageFile
import com.dragnell.myapplication.databinding.FragmentBinding
import com.dragnell.myapplication.view.act.CheckPassword
import com.dragnell.myapplication.view.act.DetailFolderAct
import com.dragnell.myapplication.view.act.FingerprintUnlock
import com.dragnell.myapplication.view.act.SetPassword
import com.dragnell.myapplication.view.adapter.FolderAdapter
import com.dragnell.myapplication.viewmodel.CommonViewModel


class FragmentAll : BaseFragment<FragmentBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        val folder = ManageFile.instance.merge("All")

        mbinding.rvGroup.adapter = FolderAdapter(
            folder,
            requireContext(),
            onClickCurrentsFolder = { },
            onClickFolder = { i ->
                ManageFile.instance.setFolder(i)
                startActivity(Intent(requireContext(),DetailFolderAct::class.java))
            },
            onClickAddFolder = { }
        )

        mbinding.locker.setOnClickListener {
            val s = CommonUtils.getInstance().getPref("Type").toString()
            if (s == "0") {
                if (checkBiometricSupport(requireContext())) {
                    CommonUtils.getInstance().savePref("isCheckBiometric", "Yes")
                    startActivity(Intent(context, FingerprintUnlock::class.java))
                } else {
                    CommonUtils.getInstance().savePref("isCheckBiometric", "No")
                    startActivity(Intent(context, SetPassword::class.java))
                }

            } else {
                startActivity(Intent(context, CheckPassword::class.java))
            }
        }
    }

    private fun checkBiometricSupport(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)

        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                false
            }

            else -> false
        }
    }


    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBinding {
        return FragmentBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = FragmentAll::class.java.name
    }
}