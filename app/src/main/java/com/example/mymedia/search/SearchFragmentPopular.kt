package com.example.mymedia.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentSearchPopularTop10Binding
import com.example.mymedia.detail.DetailActivity
import com.example.mymedia.main.MainSharedViewModel
import java.text.SimpleDateFormat
import java.util.Date


class SearchFragmentPopular : Fragment() {

    companion object {
        fun newInstance() = SearchFragmentPopular()
    }

    private var _binding: FragmentSearchPopularTop10Binding? = null
    private val binding get() = _binding!!

    private val listAdapter by lazy {
        SearchRVAdapter()
    }

    private val searchViewModel by lazy {
        ViewModelProvider(
            this, SearchViewModelFactory(ItemRepository())
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPopularTop10Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {
        rvPopularTop10.adapter = listAdapter
        rvPopularTop10.layoutManager = LinearLayoutManager(requireContext())

        searchViewModel.searchMostVideo()

        listAdapter.setOnItemClickListener(object :
            SearchRVAdapter.OnItemClickListener {
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
        searchViewModel.most.observe(viewLifecycleOwner) { itemList ->
            if(!itemList.isNullOrEmpty()){
                listAdapter.submitList(itemList.subList(0,10).toMutableList())
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