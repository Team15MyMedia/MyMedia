package com.example.mymedia.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.databinding.FragmentSearchResultBinding

class SearchFragmentResult(text: String) : Fragment() {

    private val searchText = text


    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val channelListAdapter by lazy {
        SearchResultChannelRVAdapter()
    }

    private val videosListAdapter by lazy {
        SearchResultVideoRVAdapter()
    }

    private val searchViewModel by lazy {
        ViewModelProvider(
            this, SearchViewModelFactory2(ItemRepository())
        )[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {

        //channel recyclerview
        rvChannel.adapter = channelListAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvChannel.layoutManager = layoutManager


        //videos recyclerview
        rvVideos.adapter = videosListAdapter
        rvVideos.layoutManager = GridLayoutManager(requireContext(), 3)


        searchViewModel.searchVideo(searchText)
        searchViewModel.searchByCategory(searchText)

    }

    private fun initModel() = with(binding) {
        searchViewModel.searchvideo.observe(viewLifecycleOwner) { itemList ->
            videosListAdapter.addItems(itemList.toMutableList())
        }

        searchViewModel.categoryChannel.observe(viewLifecycleOwner) { itemList ->
            channelListAdapter.addItems(itemList.toMutableList())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}