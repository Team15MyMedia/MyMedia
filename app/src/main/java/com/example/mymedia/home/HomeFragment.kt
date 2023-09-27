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
import com.example.mymedia.data.Category
import com.example.mymedia.data.CategoryItem
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentHomeBinding
import com.example.mymedia.home.adapter.HomeChannelListAdapter
import com.example.mymedia.home.adapter.HomeMostViewListAdapter
import com.example.mymedia.home.adapter.HomeCategoryVideoListAdapter


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

    // 두 번째 값만 추출한 배열
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
        // videoRecyclerView 설정
        videoRecyclerView.adapter = categoryVideoListAdapter
        val videoLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        videoRecyclerView.layoutManager = videoLayoutManager

        // channelRecyclerView 설정
        channelRecyclerView.adapter = channelListAdapter
        val channelLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        channelRecyclerView.layoutManager = channelLayoutManager

        // mostRecyclerView 설정
        mostRecyclerView.adapter = mostListAdapter
        val mostLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mostRecyclerView.layoutManager = mostLayoutManager

        // spinner 설정
        setSpinner(
            homeViewModel.categoryList.value?.map { it.title }?.toTypedArray() ?: spinnerItems
        )

        // 롱클릭 시
        categoryVideoListAdapter.setOnItemLongClickListener(object :
            HomeCategoryVideoListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(videoItem: VideoItem) {
                // 롱클릭 이벤트 처리
//                homeViewModel.showDetail(videoItem)
//                homeViewModel.searchByCategory()
                homeViewModel.getCategoryList()
            }
        })
    }

    private fun initModel() = with(binding) {
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
                (adapterView.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                val items = homeViewModel.categoryList.value?.map { it.title }?.toTypedArray()
                    ?: spinnerItems
                val selectedItem = items[position]
                val category = homeViewModel.categoryList.value
                val categoryId = category?.find { it.title == selectedItem }?.id ?: "1"

                homeViewModel.searchByCategory(categoryId)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}