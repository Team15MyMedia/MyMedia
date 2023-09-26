package com.example.mymedia.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymedia.R
import com.example.mymedia.databinding.FragmentSearchBinding
import com.example.mymedia.databinding.FragmentSearchResultBinding


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchResultFragment by lazy {
        SearchFragmentResult.newInstance()
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