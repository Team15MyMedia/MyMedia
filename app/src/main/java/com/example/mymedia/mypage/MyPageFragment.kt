package com.example.mymedia.mypage

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
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentMypageBinding
import com.example.mymedia.detail.DetailActivity
import com.example.mymedia.main.ContextProviderImpl
import com.example.mymedia.main.MainEventForDetail
import com.example.mymedia.main.MainEventForMyPage
import com.example.mymedia.main.MainSharedViewModel
import java.text.SimpleDateFormat
import java.util.Date


class MyPageFragment() : Fragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val favoriteListAdapter by lazy {
        MyPageFavoriteListAdapter()
    }

    private val mainSharedViewModel by lazy {
        ViewModelProvider(requireActivity())[MainSharedViewModel::class.java]
    }

    private val myPageViewModel by lazy {
        ViewModelProvider(
            requireActivity(), MyPageViewModelFactory(ItemRepository(), ContextProviderImpl(requireContext()))
        )[MyPageViewModel::class.java]
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
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {
        val spanCount = 2 // 가로당 아이템 수
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        favoritesRecyclerView.layoutManager = layoutManager
        favoritesRecyclerView.adapter = favoriteListAdapter


        favoriteListAdapter.setOnItemLongClickListener(object :
            MyPageFavoriteListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(videoItem: VideoItem) {
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
        // 주석 예정
        myPageViewModel.favoriteVideo.observe(viewLifecycleOwner) {
            favoriteListAdapter.submitList(it.toMutableList())
        }
        myPageViewModel.mainEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is EventForMain.CheckedItem -> {
                    mainSharedViewModel.checkVideo(event.item)
                }
            }
        }

        // mainViewModel 옵저빙
        mainSharedViewModel.myPageEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MainEventForMyPage.AddMyPageItem -> {

                }

                is MainEventForMyPage.CheckMyPageItem -> {
                    myPageViewModel.checkIsFavorite(event.item)
                }

                is MainEventForMyPage.RemoveMyPageItem -> {

                }

                is MainEventForDetail.CheckDetailItem -> {

                }

                is MainEventForMyPage.UpdateMyPageItem -> {
                    myPageViewModel.updateItem(event.item)
                }
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