package com.example.mymedia.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mymedia.R
import com.example.mymedia.home.HomeFragment
import com.example.mymedia.mypage.MyPageFragment
import com.example.mymedia.search.SearchFragment

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<MainTabs>()

    init {
        fragments.add(
            MainTabs(HomeFragment.newInstance(), R.string.home)
        )
        fragments.add(
            MainTabs(SearchFragment.newInstance(), R.string.search),
        )
        fragments.add(
            MainTabs(MyPageFragment.newInstance(), R.string.my_page),
        )
    }

    fun getFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

    fun getTitle(position: Int): Int {
        return fragments[position].titleRes
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }
}