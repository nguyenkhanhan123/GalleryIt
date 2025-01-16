package com.dragnell.myapplication.view.act

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.dragnell.myapplication.databinding.SplashBinding
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
        if (!checkAllPermissions()) {
            requestPermissions(getRequiredPermissions(), 101)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    navigateToMainActivity()
                } else {
                    managerAllFile()
                }
            } else {
                navigateToMainActivity()
            }
        }
    }

    private fun checkAllPermissions(): Boolean {
        return getRequiredPermissions().all {
            packageManager.checkPermission(it, packageName) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.CAMERA
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
    }

    private fun navigateToMainActivity() {
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
    }

    private var activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                navigateToMainActivity()
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
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
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
