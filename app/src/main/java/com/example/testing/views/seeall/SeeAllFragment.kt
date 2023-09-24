package com.example.testing.views.seeall

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testing.R
import com.example.testing.databinding.FragmentSeeAllBinding
import com.example.testing.views.adapter.SeeAllAdapter
import com.example.testing.views.adapter.SeeAllLoadStateAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SeeAllFragment : Fragment() {

    private lateinit var binding: FragmentSeeAllBinding
    private lateinit var seeAllAdapter: SeeAllAdapter
    private lateinit var loadAdapter: SeeAllLoadStateAdapter
    private val viewModel: SeeAllViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeeAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViews()
//        observeData()
    }

    private fun setupToolbar() {
        binding.apply {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(itemDetailToolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                itemDetailToolbar.apply {
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
        val path = arguments?.getString("pathData")
        val label = arguments?.getString("labelData")
        seeAllAdapter = SeeAllAdapter()
        binding.apply {
            if (path != null && label != null) {
                toolbarTitle.text = label
                lifecycleScope.launch {
                    viewModel.getMedicineByPath(path).observe(viewLifecycleOwner) {
                        seeAllAdapter.submitData(lifecycle, it)
                    }
                }
                rvListSeeAll.apply {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    adapter = seeAllAdapter.withLoadStateHeaderAndFooter(
                        SeeAllLoadStateAdapter(seeAllAdapter),
                        SeeAllLoadStateAdapter(seeAllAdapter)
                    )
                }
            }
        }
    }

}