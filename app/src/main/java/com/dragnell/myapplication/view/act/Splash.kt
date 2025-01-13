package com.dragnell.myapplication.view.act

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.dragnell.myapplication.databinding.SplashBinding
import com.dragnell.myapplication.model.FolderImg
import com.dragnell.myapplication.model.FolderVideo
import com.dragnell.myapplication.viewmodel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : BaseActivity<SplashBinding, SplashViewModel>() {

    override fun getClassVM(): Class<SplashViewModel> {
        return SplashViewModel::class.java
    }

    override fun initView() {
        if (packageManager.checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE, packageName
            ) != PackageManager.PERMISSION_GRANTED || packageManager.checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, packageName
            ) != PackageManager.PERMISSION_GRANTED || packageManager.checkPermission(
                Manifest.permission.CAMERA, packageName
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 101
            )

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    viewmodel.getImgAndVideoModel()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        val intent = Intent(this@Splash, MainActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("folderImage", viewmodel.getFolderImg())
                        bundle.putSerializable("folderVideo", viewmodel.getFolderVideo())
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    managerAllFile()
                }
            }
        }
    }

    private var activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                viewmodel.getImgAndVideoModel()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    val intent = Intent(this@Splash, MainActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("folderImage", viewmodel.getFolderImg())
                    bundle.putSerializable("folderVideo", viewmodel.getFolderVideo())
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
            } else {
                managerAllFile()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                managerAllFile()
            } else {
                finish()
            }
        }
    }

    private fun managerAllFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                activityResultLauncher.launch(intent)
            }
        }
    }

    override fun initViewBinding(): SplashBinding {
        return SplashBinding.inflate(layoutInflater)
    }
}