package com.example.mymedia.home

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val listAdapter by lazy {
        HomeVideoListAdapter()
    }

    private val homeViewModel by lazy {
        ViewModelProvider(
            this
        )[HomeViewModel::class.java]
    }

    // test Data
    val spinnerItems: MutableList<String> = mutableListOf(
        "항목 1",
        "항목 2",
        "항목 3",
        "항목 4",
        "항목 5",
        "항목 6",
        "항목 7",
        "항목 8",
        "항목 9",
        "항목 10"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {
        videoRecyclerView.adapter = listAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        videoRecyclerView.layoutManager = layoutManager

        // spinner 설정
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun initModel() = with(binding) {
        homeViewModel.search.observe(viewLifecycleOwner) { itemList ->
            listAdapter.submitList(itemList.toMutableList())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}