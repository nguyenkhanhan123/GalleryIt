package com.dragnell.myapplication.view.act

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import com.dragnell.myapplication.R
import com.dragnell.myapplication.databinding.ActivityMainBinding
import com.dragnell.myapplication.view.adapter.CommonTabAdapter
import com.dragnell.myapplication.view.fragment.FragmentAll
import com.dragnell.myapplication.view.fragment.FragmentImage
import com.dragnell.myapplication.view.fragment.FragmentVideo
import com.dragnell.myapplication.viewmodel.CommonViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity<ActivityMainBinding,CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {

        val listFragment: ArrayList<Fragment> = arrayListOf(
            FragmentAll(),FragmentImage(), FragmentVideo()
        )
        val tabIds: ArrayList<Long> = arrayListOf(
            resources.getString(R.string.all).hashCode().toLong(),
            resources.getString(R.string.image).hashCode().toLong(),
            resources.getString(R.string.video).hashCode().toLong()
        )
        val pagerAdapter = CommonTabAdapter(this, listFragment, tabIds)
        mbinding.viewPager.adapter = pagerAdapter
        val array: Array<String> = resources.getStringArray(R.array.tablayout)
        TabLayoutMediator(mbinding.tablayout, mbinding.viewPager, false, false) { tab, position ->
            tab.text = array[position]
        }.attach()

        mbinding.camera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivity(takePictureIntent)
            }
        }
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = MainActivity::class.java.name
    }
}