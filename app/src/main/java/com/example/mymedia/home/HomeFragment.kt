package com.example.mymedia.home

import android.R
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mymedia.data.Category
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentHomeBinding
import com.example.mymedia.home.adapter.HomeBannerListAdapter
import com.example.mymedia.home.adapter.HomeCategoryVideoListAdapter
import com.example.mymedia.home.adapter.HomeChannelListAdapter
import com.example.mymedia.home.adapter.HomeMostViewListAdapter


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoryVideoListAdapter by lazy {
        HomeCategoryVideoListAdapter()
    }

    private val channelListAdapter by lazy {
        HomeChannelListAdapter()
    }

    private val mostListAdapter by lazy {
        HomeMostViewListAdapter()
    }

    private val bannerListAdapter by lazy {
        HomeBannerListAdapter()
    }

    private val homeViewModel by lazy {
        ViewModelProvider(
            this, SearchViewModelFactory(ItemRepository())
        )[HomeViewModel::class.java]
    }

    // test Data
    private val categories: MutableList<Category> = mutableListOf(
        Category("0", "게임"),
        Category("1", "영화"),
        Category("2", "뉴스"),
        Category("3", "음악"),
        Category("4", "실시간"),
        Category("5", "피트니스"),
        Category("6", "최근에 업로드 된 영상")
    )

    // test 두 번째 값만 추출한 배열
    private val spinnerItems: Array<String> = categories.map { it.title }.toTypedArray()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {
        // bannerRecyclerView 설정
        bannerRecyclerView.adapter = bannerListAdapter
        val bannerVideoLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bannerRecyclerView.layoutManager = bannerVideoLayoutManager

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(bannerRecyclerView)

        val listener = SnapPagerScrollListener(
            snapHelper,
            SnapPagerScrollListener.ON_SCROLL,
            true,
            object : SnapPagerScrollListener.OnChangeListener {
                override fun onSnapped(position: Int) {
                    // position을 받아서 이벤트 처리
                }
            }
        )
        bannerRecyclerView.addOnScrollListener(listener)
        bannerRecyclerView.addItemDecoration(LinePagerIndicatorDecoration())

        // categoryRecyclerView 설정
        categoryRecyclerView.adapter = categoryVideoListAdapter
        val categoryVideoLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.layoutManager = categoryVideoLayoutManager

        // categoryRecyclerView 스크롤 이벤트 감지
        categoryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    categoryVideoLayoutManager.findLastCompletelyVisibleItemPosition()

                val itemCount = categoryVideoListAdapter.itemCount

                // 스크롤이 마지막에 도달하기 2번째 전일때 처리
                if (lastVisibleItemPosition == itemCount - 2 && itemCount >= 2) {
                    homeViewModel.reorganizeOrder("category")
                }
            }
        })

        // channelRecyclerView 설정
        channelRecyclerView.adapter = channelListAdapter
        val channelLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        channelRecyclerView.layoutManager = channelLayoutManager

        // channelRecyclerView 스크롤 이벤트 감지
        channelRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    channelLayoutManager.findLastCompletelyVisibleItemPosition()

                val itemCount = channelListAdapter.itemCount

                // 스크롤이 마지막에 도달하기 2번째 전일때 처리
                if (lastVisibleItemPosition == itemCount - 2 && itemCount >= 2) {
                    homeViewModel.reorganizeOrder("channel")
                }
            }
        })

        // mostRecyclerView 설정
        mostRecyclerView.adapter = mostListAdapter
        val mostLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mostRecyclerView.layoutManager = mostLayoutManager

        mostRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    mostLayoutManager.findLastCompletelyVisibleItemPosition()

                val itemCount = mostListAdapter.itemCount

                // 스크롤이 마지막에 도달하기 2번째 전일때 처리
                if (lastVisibleItemPosition == itemCount - 2 && itemCount >= 2) {
                    homeViewModel.reorganizeOrder("most")
                }
            }
        })

        // spinner 설정
        setSpinner(
            homeViewModel.categoryList.value?.map { it.title }?.toTypedArray() ?: spinnerItems
        )

        // 롱클릭 시
        categoryVideoListAdapter.setOnItemClickListener(object :
            HomeCategoryVideoListAdapter.OnItemClickListener {
            override fun onItemClick(videoItem: VideoItem) {
                // 롱클릭 이벤트 처리
                homeViewModel.showDetail(videoItem, requireContext())
            }
        })

        // test
        bannerListAdapter.setOnItemLongClickListener(object :
            HomeBannerListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(videoItem: VideoItem) {
                // 롱클릭 이벤트 처리
                homeViewModel.showByYoutube(videoItem, requireContext())
            }
        })
    }

    private fun initModel() = with(binding) {
        homeViewModel.mostLive.observe(viewLifecycleOwner) { itemList ->
            bannerListAdapter.submitList(itemList.toMutableList())
        }
        homeViewModel.categoryVideo.observe(viewLifecycleOwner) { itemList ->
            categoryVideoListAdapter.submitList(itemList.toMutableList())
        }
        homeViewModel.categoryChannel.observe(viewLifecycleOwner) { itemList ->
            channelListAdapter.submitList(itemList.toMutableList())
        }
        homeViewModel.most.observe(viewLifecycleOwner) { itemList ->
            mostListAdapter.submitList(itemList.toMutableList())
        }
        homeViewModel.categoryList.observe(viewLifecycleOwner) { itemList ->
            setSpinner(itemList.map { it.title }.toTypedArray())
        }
    }

    private fun setSpinner(list: Array<String>) = with(binding) {
        val spinnerAdapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val text = view.findViewById<TextView>(R.id.text1)
                text.textSize = 25F
                text.setTextColor(Color.WHITE)
                return view
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val text = view.findViewById<TextView>(R.id.text1)
                text.textSize = 25F
                text.setTextColor(Color.WHITE)
                return view
            }
        }
        spinner.background.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                l: Long
            ) {
                val items = homeViewModel.categoryList.value?.map { it.title }?.toTypedArray()
                    ?: spinnerItems
                val selectedItem = items[position]
                val category = homeViewModel.categoryList.value
                val categoryId = category?.find { it.title == selectedItem }?.id ?: "1"

                homeViewModel.searchByCategory(categoryId)
                homeViewModel.setCurCategory(position)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        // 초기 선택값 설정
        spinner.setSelection(homeViewModel.curCategory.value ?: 0)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}