package com.example.mymedia.search

import android.app.appsearch.SearchResult
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.MediaItem
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentSearchPopularTop10Binding
import com.example.mymedia.databinding.FragmentSearchResultBinding
import com.example.mymedia.home.HomeViewModel
import com.example.mymedia.home.SearchViewModelFactory
import kotlinx.coroutines.launch

class SearchFragmentResult(text: String) : Fragment() {

    private val searchText = text


    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val CHANNEL_VIEW_TYPE = 1
    private val VIDEOS_VIEW_TYPE = 2

    private val channelListAdapter by lazy {
        SearchResultRVAdapter(CHANNEL_VIEW_TYPE)
    }

    private val videosListAdapter by lazy {
        SearchResultRVAdapter(VIDEOS_VIEW_TYPE)
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

    }

    private fun initModel() = with(binding) {
        searchViewModel.searchvideo.observe(viewLifecycleOwner) { itemList ->
            videosListAdapter.addItems(itemList.toMutableList())
        }

        searchViewModel.categoryChannel.observe(viewLifecycleOwner) { itemList ->
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}