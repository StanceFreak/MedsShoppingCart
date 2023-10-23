package com.example.testing.views.seeall

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.data.api.model.response.CategoryListData
import com.example.testing.data.api.model.response.HomeParentResponse
import com.example.testing.databinding.FragmentSeeAllCategoryBinding
import com.example.testing.views.adapter.HomeCategoryAdapter
import com.example.testing.views.adapter.SeeAllCategoryAdapter
import org.koin.android.ext.android.inject

class SeeAllCategoryFragment : Fragment() {

    private lateinit var binding: FragmentSeeAllCategoryBinding
    private lateinit var seeAllCategoryAdapter: SeeAllCategoryAdapter
    private val viewModel: SeeAllCategoryViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeeAllCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViews()
        observeData()
    }

    private fun setupToolbar() {
        binding.apply {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbarSeeAllCategory)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                toolbarSeeAllCategory.apply {
                    setNavigationIcon(R.drawable.ic_back)
                    title = ""
                    setNavigationOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
    }

    private fun setupViews() {
        val label = arguments?.getString("labelData")
        if (label != null) {
//                seeAllCategoryAdapter = SeeAllCategoryAdapter(dataList)
            viewModel.getCategoryList()
            binding.tvTitleSeeAllCategory.text = label
//                rvListSeeAllCategory.apply {
//                    addItemDecoration(
//                        DividerItemDecoration(
//                            requireContext(),
//                            LinearLayoutManager.VERTICAL
//                        )
//                    )
//                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//                    adapter = seeAllCategoryAdapter
//                }
        }
    }

    private fun observeData() {
        val dataList = arguments?.getParcelableArrayList<CategoryListData>("categoryData")
        viewModel.apply {
            binding.apply {
                observeGetCategoryListSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        if (response.first == true && dataList != null) {
                            shimmerLoadingSeeAllCategory.isGone = true
                            seeAllCategoryAdapter = SeeAllCategoryAdapter(dataList)
                            rvListSeeAllCategory.apply {
                                isGone = false
                                addItemDecoration(
                                    DividerItemDecoration(
                                        requireContext(),
                                        LinearLayoutManager.VERTICAL
                                    )
                                )
                                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                                adapter = seeAllCategoryAdapter
                            }
                        }
                    }
                }
                observeGetCategoryListLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        rvListSeeAllCategory.isGone = loading
                        shimmerLoadingSeeAllCategory.isGone = !loading
                    }
                }
                observeGetCategoryListError().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { error ->
                        if (error.second != null) {
                            rvListSeeAllCategory.isGone = error.first
                            shimmerLoadingSeeAllCategory.isGone = error.first
                            Toast.makeText(requireContext(), error.second, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    }

}