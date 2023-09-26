package com.example.mymedia.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymedia.R
import com.example.mymedia.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPager by lazy { binding.viewPager }
    private val tabLayout by lazy { binding.tabLayout }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val adapter = MainViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = adapter.itemCount


        // 뷰페이저 스와이프 막기
        viewPager.run {
            isUserInputEnabled = false
        }


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "홈"
                1 -> "검색"
                2 -> "마이 페이지"
                else -> null
            }
        }.attach()

        // 모든 탭에 아이콘 설정
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                updateTabIconsAndText(tab, i == 0)
            }
        }

        // 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateTabIconsAndText(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                updateTabIconsAndText(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })
    }

    private fun updateTabIconsAndText(tab: TabLayout.Tab?, isSelected: Boolean) {
        val position = tab?.position ?: return

        // 선택된 탭의 아이콘 및 텍스트 업데이트
        when (position) {
            0 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_sel_home_bt else R.drawable.group_51)
                tab.text = if (isSelected) "홈" else "홈"
            }
            1 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_sel_search_bt else R.drawable.group_48_1)
                tab.text = if (isSelected) "검색" else "검색"
            }
            2 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_sel_mypage_bt else R.drawable.group_3)
                tab.text = if (isSelected) "마이 페이지" else "마이 페이지"
            }
        }
    }
}
