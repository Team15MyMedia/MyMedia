package com.example.mymedia.home

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.data.Item
import com.example.mymedia.databinding.FragmentHomeBinding
import com.example.mymedia.home.adapter.HomeChannelListAdapter
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

    private val homeViewModel by lazy {
        ViewModelProvider(
            this
        )[HomeViewModel::class.java]
    }

    // test Data
    val spinnerItems: MutableList<String> = mutableListOf(
        "항목 1",
        "항목 2",
        "항목 3",
        "항목 4",
        "항목 5",
        "항목 6",
        "항목 7",
        "항목 8",
        "항목 9",
        "항목 10"
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

        // spinner 설정
        val spinnerAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, spinnerItems)
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // 롱클릭 시
        videoListAdapter.setOnItemLongClickListener(object :
            HomeVideoListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(item: Item) {
                // 롱클릭 이벤트 처리
                homeViewModel.showDetail(item)
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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}