package com.dragnell.myapplication.view.fragment


import androidx.biometric.BiometricManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dragnell.myapplication.CommonUtils
import com.dragnell.myapplication.ManageFile
import com.dragnell.myapplication.databinding.FragmentBinding
import com.dragnell.myapplication.model.Folder
import com.dragnell.myapplication.model.FolderImg
import com.dragnell.myapplication.model.FolderVideo
import com.dragnell.myapplication.view.act.CheckPassword
import com.dragnell.myapplication.view.act.FingerprintUnlock
import com.dragnell.myapplication.view.act.SetPassword
import com.dragnell.myapplication.view.adapter.FolderAdapter
import com.dragnell.myapplication.viewmodel.CommonViewModel


class FragmentAll : BaseFragment<FragmentBinding, CommonViewModel>() {

    private var folderImage : ArrayList<FolderImg> = ArrayList()
    private var folderVideo : ArrayList<FolderVideo> = ArrayList()

    override fun getClassVM(): Class<CommonViewModel> {
       return CommonViewModel::class.java
    }

    override fun initView() {
        folderImage=ManageFile.instance.getFolderImg()

        folderVideo=ManageFile.instance.getFolderVideo()

        val folder: ArrayList<Any> = ArrayList()

        folder.addAll(mergeFolders(folderImage,folderVideo))

        mbinding.rvGroup.adapter= FolderAdapter(folder,requireContext()){

        }

        mbinding.locker.setOnClickListener {
           val s = CommonUtils.getInstance().getPref("Type").toString()
            if (s=="0"){
                if (checkBiometricSupport(requireContext())){
                    CommonUtils.getInstance().savePref("isCheckBiometric","Yes")
                    startActivity(Intent(context, FingerprintUnlock::class.java))
                }
                else{
                    CommonUtils.getInstance().savePref("isCheckBiometric","No")
                    startActivity(Intent(context, SetPassword::class.java))
                }

            }
            else{
                startActivity(Intent(context, CheckPassword::class.java))
            }
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

    private fun checkBiometricSupport(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)

        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Thiết bị hỗ trợ sinh trắc học
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // Thiết bị không có phần cứng sinh trắc học
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Không có sinh trắc học nào được đăng ký
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