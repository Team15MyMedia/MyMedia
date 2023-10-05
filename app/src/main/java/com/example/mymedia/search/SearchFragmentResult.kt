package com.example.mymedia.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentSearchResultBinding
import com.example.mymedia.detail.DetailActivity
import com.example.mymedia.main.MainSharedViewModel
import java.text.SimpleDateFormat
import java.util.Date

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
            requireActivity(), SearchViewModelFactory(ItemRepository())
        )[SearchViewModel::class.java]
    }

    private val mainSharedViewModel by lazy {
        ViewModelProvider(requireActivity())[MainSharedViewModel::class.java]
    }

    private val detailActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val videoId = data?.getStringExtra("id") ?: ""
                val videoTitle = data?.getStringExtra("title") ?: ""
                val videoDescription = data?.getStringExtra("description") ?: ""
                val dateString = data?.getStringExtra("datetime") ?: ""
                val videoThumbnail = data?.getStringExtra("thumbnail") ?: ""
                val isFavorite = data?.getBooleanExtra("isFavorite", false) ?: false
                val videoNextPage = data?.getStringExtra("nextPage") ?: ""
                val videoChannelId = data?.getStringExtra("channelId") ?: ""

                val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"

                val videoDatetime = stringToDate(dateString, dateFormat) ?: Date()

                val item = VideoItem(
                    videoId,
                    videoTitle,
                    videoDescription,
                    videoDatetime,
                    videoThumbnail,
                    isFavorite,
                    videoNextPage,
                    videoChannelId
                )
                mainSharedViewModel.updateData(item)
            }
        }

    private lateinit var gridmanager: GridLayoutManager
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
        gridmanager = GridLayoutManager(requireContext(), 3)
        rvVideos.layoutManager = gridmanager
        rvVideos.addOnScrollListener(onScrollListener)
        rvVideos.itemAnimator = null



        videosListAdapter.setOnItemClickListener(object : SearchResultVideoRVAdapter.OnItemClickListener{
            override fun onItemClick(videoItem: VideoItem) {
                mainSharedViewModel.isFavorite(videoItem)
                if (mainSharedViewModel.selItem != null) {
                    showDetail(videoItem.copy(isFavorite = true))
                } else {
                    showDetail(videoItem)
                }
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

                val firstVisibleItemPosition = gridmanager.findFirstVisibleItemPosition()

                //현재 화면에 아이템이 보이는지 여부를 확인
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                    pastVisibleItems = firstVisibleItemPosition
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

    fun showDetail(videoItem: VideoItem) {
        val intent = Intent(context, DetailActivity::class.java)

        intent.putExtra(
            "videoThumbnail",
            videoItem.thumbnail.replace("/default.jpg", "/maxresdefault.jpg")
        )
        intent.putExtra("videoTitle", videoItem.title)
        intent.putExtra("videoDescription", videoItem.description)
        intent.putExtra("isFavorite", videoItem.isFavorite)
        intent.putExtra("videoId", videoItem.id)
        intent.putExtra("videoDatetime", videoItem.datetime)
        intent.putExtra("videoNextPage", videoItem.nextPage)
        intent.putExtra("videoChannelId", videoItem.channelId)

        detailActivityResultLauncher.launch(intent)
    }

    private fun stringToDate(dateString: String, dateFormat: String): Date? {
        return try {
            val sdf = SimpleDateFormat(dateFormat)
            sdf.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}