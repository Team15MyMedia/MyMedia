package com.example.mymedia.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentHomeBinding
import com.example.mymedia.databinding.FragmentMypageBinding
import com.example.mymedia.home.HomeViewModel
import com.example.mymedia.home.SearchViewModelFactory
import com.example.mymedia.home.adapter.HomeCategoryVideoListAdapter


class MyPageFragment : Fragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val categoryVideoListAdapter by lazy {
        MyPageFavoriteListAdapter()
    }

    private val myPageViewModel by lazy {
        ViewModelProvider(
            this, MyPageViewModelFactory(ItemRepository())
        )[MyPageViewModel::class.java]
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
        favoritesRecyclerView.adapter = categoryVideoListAdapter

        categoryVideoListAdapter.setOnItemLongClickListener(object :
            MyPageFavoriteListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(videoItem: VideoItem) {
                // 롱클릭 이벤트 처리
                myPageViewModel.showDetail(videoItem, requireContext())
            }
        })
    }

    private fun initModel() = with(binding) {
        myPageViewModel.favoriteVideo.observe(viewLifecycleOwner) {
            categoryVideoListAdapter.submitList(it.toMutableList())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}