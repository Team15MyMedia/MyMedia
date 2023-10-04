package com.example.mymedia.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mymedia.R
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.databinding.FragmentSearchBinding
import com.example.mymedia.databinding.FragmentSearchResultBinding
import com.example.mymedia.home.HomeViewModel
import com.example.mymedia.home.SearchViewModelFactory


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchResultFragment by lazy {
        SearchFragmentResult.newInstance()
    }

    private val searchViewModel by lazy {
        ViewModelProvider(
            requireActivity(), SearchViewModelFactory2(ItemRepository())
        )[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()

    }

    private fun initView() = with(binding) {

        imbSearch.setOnClickListener {

            searchViewModel.list.clear()
            searchViewModel.searchText = edtSearch.text.toString()

            searchViewModel.searchVideo(searchViewModel.searchText, "")
            searchViewModel.searchChannel(searchViewModel.searchText, "")

            val fragmentResult = searchResultFragment
            val transaction = requireFragmentManager().beginTransaction()

            transaction.replace(R.id.fragmentContainerView, fragmentResult)
            transaction.addToBackStack(null)
            transaction.commit()

        }

    }

    private fun initModel() = with(binding) {
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}