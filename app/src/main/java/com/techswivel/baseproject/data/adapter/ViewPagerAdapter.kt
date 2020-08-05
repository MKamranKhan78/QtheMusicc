package com.techswivel.baseproject.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(
    fm: FragmentManager,
    private var fragmentList: List<Any>,
    private var titleList: List<Any>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return fragmentList[position] as Fragment
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position] as String
    }

    interface CallBack {
        fun getItemForPosition(position: Int): Fragment
    }
}