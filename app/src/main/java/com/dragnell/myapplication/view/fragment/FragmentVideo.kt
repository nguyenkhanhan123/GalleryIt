package com.dragnell.myapplication.view.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dragnell.myapplication.ManageFile
import com.dragnell.myapplication.databinding.FragmentBinding
import com.dragnell.myapplication.view.act.DetailFolderAct
import com.dragnell.myapplication.view.act.FingerprintUnlock
import com.dragnell.myapplication.view.adapter.FolderAdapter
import com.dragnell.myapplication.viewmodel.CommonViewModel

class FragmentVideo : BaseFragment<FragmentBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
       return CommonViewModel::class.java
    }

    override fun initView() {
        val folder = ManageFile.instance.merge("Video")

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
            startActivity(Intent(context, FingerprintUnlock::class.java))
        }
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBinding {
        return FragmentBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = FragmentVideo::class.java.name
    }
}