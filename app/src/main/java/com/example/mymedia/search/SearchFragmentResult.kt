package com.example.mymedia.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.databinding.FragmentSearchPopularTop10Binding
import com.example.mymedia.databinding.FragmentSearchResultBinding

class SearchFragmentResult : Fragment() {

    companion object {
        fun newInstance() = SearchFragmentResult()
    }

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val listAdapter by lazy {
        SearchRVAdapter()
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
    }

    private fun initModel() = with(binding) {

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}