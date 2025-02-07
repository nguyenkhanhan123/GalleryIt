package com.dragnell.myapplication.view.act

import androidx.recyclerview.widget.GridLayoutManager
import com.dragnell.myapplication.ManageFile
import com.dragnell.myapplication.databinding.DetailFolderActBinding
import com.dragnell.myapplication.view.adapter.FileAdapter
import com.dragnell.myapplication.view.customview.ZoomableRecyclerView
import com.dragnell.myapplication.viewmodel.CommonViewModel

class DetailFolderAct : BaseActivity<DetailFolderActBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        val folder = ManageFile.instance.getFolder()
        if (folder != null) {
            val layoutManager = GridLayoutManager(this, 3)
            mbinding.rvFile.layoutManager = layoutManager
            mbinding.rvFile.adapter = FileAdapter(folder.list!!, this)
        }
    }


    override fun initViewBinding(): DetailFolderActBinding {
        return DetailFolderActBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = DetailFolderAct::class.java.name
    }
}