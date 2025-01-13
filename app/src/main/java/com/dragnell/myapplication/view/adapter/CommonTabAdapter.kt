package com.dragnell.myapplication.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class CommonTabAdapter(
    fragmentActivity: FragmentActivity,
    private val listScreen: ArrayList<Fragment>,
    private val fragmentIds: ArrayList<Long>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = listScreen.size


    override fun createFragment(position: Int): Fragment {
        return listScreen[position]
    }

    override fun getItemId(position: Int): Long {
        return fragmentIds[position]
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragmentIds.contains(itemId)
    }

}
