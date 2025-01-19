package com.dragnell.myapplication.view.act

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.TypedValue
import android.view.animation.LinearInterpolator
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.dragnell.myapplication.ManageFile
import com.dragnell.myapplication.databinding.SplashBinding
import com.dragnell.myapplication.viewmodel.CommonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Splash : BaseActivity<SplashBinding, CommonViewModel>() {

    private lateinit var animator : ObjectAnimator

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        animator = ObjectAnimator.ofFloat(mbinding.includedLoading.view, "translationX", 0f, dpToPx(this,142f))
        animator.duration = 850
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.RESTART
        animator.interpolator = LinearInterpolator()
        animator.start()
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

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        )
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
        CoroutineScope(Dispatchers.Main).launch {
            loadData()
            animator.end()
            startActivity(Intent(this@Splash, MainActivity::class.java))
            finish()
        }
    }

    private suspend fun loadData() = withContext(Dispatchers.IO) {
        ManageFile.instance.getImgAndVideoModel()
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
