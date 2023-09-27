package com.example.mymedia.search

import android.os.Bundle
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

//        val item = com.example.mymedia.data.Data
//        val data = item.getSearchData()
//        listAdapter.addItems(data.toList())

        searchViewModel.searchMostVideo()
    }

    private fun initModel() = with(binding) {
        val item10List = mutableListOf<VideoItem>()
        searchViewModel.most.observe(viewLifecycleOwner) { itemList ->
            item10List.clear()
            for(i in 0 .. 9){
                item10List.add(itemList[i])
            }
            listAdapter.addItems(item10List)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}