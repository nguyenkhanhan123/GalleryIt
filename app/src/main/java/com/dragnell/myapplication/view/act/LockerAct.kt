package com.dragnell.myapplication.view.act

import android.content.Context
import android.os.Environment
import android.util.Log
import com.dragnell.myapplication.databinding.LockerActBinding
import com.dragnell.myapplication.viewmodel.CommonViewModel
import java.io.File

class LockerAct : BaseActivity<LockerActBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        createHiddenFolder(this, "An")
    }

    private fun createHiddenFolder(context: Context, folderName: String) {
        val externalDir = Environment.getExternalStorageDirectory()
        val hiddenFolder = File(externalDir, ".$folderName")
        if (!hiddenFolder.exists()) {
            Log.i("Toast","Tạo file")
            hiddenFolder.mkdir()
        }
        else{
            Log.i("Toast","Thư mục đã tồn tại")
        }
    }


    override fun initViewBinding(): LockerActBinding {
        return LockerActBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = LockerAct::class.java.name
    }
}