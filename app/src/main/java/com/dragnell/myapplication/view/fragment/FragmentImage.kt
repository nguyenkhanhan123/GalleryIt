package com.dragnell.myapplication.view.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dragnell.myapplication.CommonUtils
import com.dragnell.myapplication.ManageFile
import com.dragnell.myapplication.databinding.FragmentBinding
import com.dragnell.myapplication.view.act.CheckPassword
import com.dragnell.myapplication.view.act.DetailFolderAct
import com.dragnell.myapplication.view.act.FingerprintUnlock
import com.dragnell.myapplication.view.adapter.FolderAdapter
import com.dragnell.myapplication.viewmodel.CommonViewModel

class FragmentImage : BaseFragment<FragmentBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
       return CommonViewModel::class.java
    }

    override fun initView() {
        val folder = ManageFile.instance.merge("Image")

        mbinding.rvGroup.adapter = FolderAdapter(
            folder,
            requireContext(),
            onClickCurrentsFolder = { },
            onClickFolder = {  i ->
                ManageFile.instance.setFolder(i)
                startActivity(Intent(requireContext(), DetailFolderAct::class.java))
            },
            onClickAddFolder = { }
        )

        mbinding.locker.setOnClickListener {
            val s = CommonUtils.getInstance().getPref("Type").toString()
            if (s=="0"){
                startActivity(Intent(context, FingerprintUnlock::class.java))
            }
            else{
                startActivity(Intent(context, CheckPassword::class.java))
            }
        }
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBinding {
        return FragmentBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = FragmentImage::class.java.name
    }
}