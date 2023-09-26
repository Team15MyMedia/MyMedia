package com.example.mymedia.home

import android.R
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
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
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentHomeBinding
import com.example.mymedia.home.adapter.HomeChannelListAdapter
import com.example.mymedia.home.adapter.HomeMostViewListAdapter
import com.example.mymedia.home.adapter.HomeVideoListAdapter


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val videoListAdapter by lazy {
        HomeVideoListAdapter()
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
    val spinnerItems: MutableList<String> = mutableListOf(
        "게임",
        "영화",
        "뉴스",
        "음악",
        "실시간",
        "피트니스",
        "최근에 업로드 된 영상",
    )

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
        videoRecyclerView.adapter = videoListAdapter
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
        val spinnerAdapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, spinnerItems) {
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
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                (adapterView.getChildAt(0) as TextView).setTextColor(Color.WHITE)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        // 롱클릭 시
        videoListAdapter.setOnItemLongClickListener(object :
            HomeVideoListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(videoItem: VideoItem) {
                // 롱클릭 이벤트 처리
                homeViewModel.showDetail(videoItem)
                homeViewModel.searchCategoryVideo()
            }
        })

        // test
        mostListAdapter.setOnItemLongClickListener(object :
            HomeMostViewListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(videoItem: VideoItem) {
                // 롱클릭 이벤트 처리
                homeViewModel.searchMostVideo()
            }
        })
    }

    private fun initModel() = with(binding) {
        homeViewModel.video.observe(viewLifecycleOwner) { itemList ->
            videoListAdapter.submitList(itemList.toMutableList())
        }
        homeViewModel.channel.observe(viewLifecycleOwner) { itemList ->
            channelListAdapter.submitList(itemList.toMutableList())
        }
        homeViewModel.most.observe(viewLifecycleOwner) { itemList ->
            mostListAdapter.submitList(itemList.toMutableList())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}