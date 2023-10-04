package com.example.mymedia.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentSearchResultBinding

class SearchFragmentResult() : Fragment() {

    companion object{
        fun newInstance() = SearchFragmentResult()
    }


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
            requireActivity(), SearchViewModelFactory2(ItemRepository())
        )[SearchViewModel::class.java]
    }

    private lateinit var gridmanager: StaggeredGridLayoutManager
    private lateinit var channelGridmanager: StaggeredGridLayoutManager
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisibleItems = 0
    private var channelVisibleItemCount = 0
    private var channelTotalItemCount = 0
    private var channelPastVisibleItems = 0
    private var loading = true

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
        channelGridmanager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        rvChannel.layoutManager = channelGridmanager
        rvChannel.addOnScrollListener(onScrollChannelListener)
        rvChannel.itemAnimator = null


        //videos recyclerview
        rvVideos.adapter = videosListAdapter
        gridmanager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        rvVideos.layoutManager = gridmanager
        rvVideos.addOnScrollListener(onScrollListener)
        rvVideos.itemAnimator = null

        videosListAdapter.setOnItemClickListener(object : SearchResultVideoRVAdapter.OnItemClickListener{
            override fun onItemClick(videoItem: VideoItem) {
                searchViewModel.showDetail(videoItem, requireContext())
            }
        })

    }

    private fun initModel() = with(binding) {
        searchViewModel.searchvideo.observe(viewLifecycleOwner) { itemList ->
            videosListAdapter.submitList(itemList.toMutableList())
        }
        searchViewModel.searchChannel.observe(viewLifecycleOwner) { itemList ->
            channelListAdapter.submitList(itemList.toMutableList())
        }
        searchViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loading = !isLoading
        }
    }

    private var onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                visibleItemCount = gridmanager.childCount
                totalItemCount = gridmanager.itemCount

                val firstVisibleItems = gridmanager.findFirstVisibleItemPositions(null)
                if (firstVisibleItems.isNotEmpty()) {
                    pastVisibleItems = firstVisibleItems[0]
                }

                if (loading && visibleItemCount + pastVisibleItems >= totalItemCount) {
                    loading = false
                    searchViewModel.doSearch(searchViewModel.searchText)
                }
            }
        }

    private var onScrollChannelListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                channelVisibleItemCount = channelGridmanager.childCount
                channelTotalItemCount = channelGridmanager.itemCount

                val firstVisibleItems = channelGridmanager.findFirstVisibleItemPositions(null)
                if (firstVisibleItems.isNotEmpty()) {
                    channelPastVisibleItems = firstVisibleItems[0]
                }

                if (loading && channelVisibleItemCount + channelPastVisibleItems >= channelTotalItemCount) {
                    loading = false
                    searchViewModel.doChannelSearch(searchViewModel.searchText)
                }
            }
        }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}