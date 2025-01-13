package com.dragnell.myapplication.view.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dragnell.myapplication.databinding.FragmentBinding
import com.dragnell.myapplication.model.Folder
import com.dragnell.myapplication.model.FolderImg
import com.dragnell.myapplication.model.FolderVideo
import com.dragnell.myapplication.view.act.FingerprintUnlock
import com.dragnell.myapplication.view.act.MainActivity
import com.dragnell.myapplication.view.adapter.FolderAdapter
import com.dragnell.myapplication.viewmodel.CommonViewModel

class FragmentImage : BaseFragment<FragmentBinding, CommonViewModel>() {

    private var folderImage : ArrayList<FolderImg> = ArrayList()

    override fun getClassVM(): Class<CommonViewModel> {
       return CommonViewModel::class.java
    }

    override fun initView() {
        arguments?.let {
            folderImage = (it.getSerializable("folderImage") as? ArrayList<FolderImg>)!!
        }

        val folder: ArrayList<Any> = ArrayList()

        folder.addAll(mergeFolders(folderImage,null))

        mbinding.rvGroup.adapter=FolderAdapter(folder,requireContext()){

        }

        mbinding.locker.setOnClickListener {
            startActivity(Intent(context, FingerprintUnlock::class.java))
        }
    }

    private fun mergeFolders(
        folderImgs: ArrayList<FolderImg>?,
        folderVideos: ArrayList<FolderVideo>?
    ): ArrayList<Folder> {
        val folderMap = mutableMapOf<String, ArrayList<Any>>()

        folderImgs?.forEach { folderImg ->
            val name = folderImg.name ?: return@forEach
            val list = folderImg.listImg ?: return@forEach

            folderMap.getOrPut(name) { arrayListOf() }.addAll(list)
        }

        folderVideos?.forEach { folderVideo ->
            val name = folderVideo.name ?: return@forEach
            val list = folderVideo.listVideo ?: return@forEach

            folderMap.getOrPut(name) { arrayListOf() }.addAll(list)
        }

        val mergedFolders = ArrayList<Folder>()
        folderMap.forEach { (name, list) ->
            mergedFolders.add(Folder(name = name, list = list))
        }

        return mergedFolders
    }



    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBinding {
        return FragmentBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = FragmentImage::class.java.name
    }
}