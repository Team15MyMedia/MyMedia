package com.example.mymedia.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.FragmentSearchPopularTop10Binding
import com.example.mymedia.home.HomeViewModel
import com.example.mymedia.home.SearchViewModelFactory


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
            this, SearchViewModelFactory2(ItemRepository())
        )[SearchViewModel::class.java]
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
    }

    private fun initModel() = with(binding) {
        searchViewModel.most.observe(viewLifecycleOwner) { itemList ->
            listAdapter.submitList(itemList.subList(0,10).toMutableList())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}