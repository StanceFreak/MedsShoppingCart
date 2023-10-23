package com.example.testing.views.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.data.api.model.response.HomeParentResponse
import com.example.testing.databinding.FragmentHomeBinding
import com.example.testing.views.adapter.HomeParentAdapter
import com.example.testing.views.adapter.HomeSliderPromoAdapter
import com.smarteist.autoimageslider.SliderView
import org.koin.android.ext.android.inject

//@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by inject()
    private val parentData = ArrayList<HomeParentResponse>()
    private lateinit var homeParentAdapter: HomeParentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAdapter()
        setupViews()
        observeData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupAdapter() {
        homeParentAdapter = HomeParentAdapter()
        binding.apply {
            rvParentHome.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = homeParentAdapter
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            parentData.clear()
            viewModel.getHomeData()
            homeSwipeRefresh.setOnRefreshListener {
                homeSwipeRefresh.isRefreshing = false
                homeParentAdapter.notifyDataSetChanged()
            }
            ivCartHome.setOnClickListener {
                findNavController().navigate(R.id.home_to_shopping)
            }
        }
    }

    private fun observeData() {
        val responseData = ArrayList<HomeParentResponse>()
        binding.apply {
            viewModel.apply {
                observeGetOfferSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        responseData.add(HomeParentResponse("Penawaran Spesial", "penawaran-special", 3,  response.medicineList, null))
                        parentData.clear()
                        parentData.addAll(responseData)
                        homeParentAdapter.setData(parentData, 3)
//                        homeParentAdapter.setDataSlider(parentData, 3)
                    }
                }
                observeGetParentingSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        responseData.add(HomeParentResponse("Kebutuhan Ibu & Anak", "ibu-dan-anak", 1,  response.medicineList, null))
                        parentData.clear()
                        parentData.addAll(responseData)
                        homeParentAdapter.setData(parentData, 1)
                    }
                }
                observeGetCategoryListSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        if (response.first == true) {
                            responseData.add(HomeParentResponse("Kategori lainnya", null, 4,  null, null))
                            parentData.clear()
                            parentData.addAll(responseData)
                            homeParentAdapter.setData(parentData, 4)
                        }
                    }
                }
                observeGetDiabetSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        responseData.add(HomeParentResponse("Perawatan Diabetes", "diabetes-medicine", 1,  response.medicineList, null))
                        parentData.clear()
                        parentData.addAll(responseData)
                        homeParentAdapter.setData(parentData, 1)
                    }
                }
                observeGetTrendingArticlesSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        responseData.add(HomeParentResponse("Artikel Terkini", null, 2, null, response))
                        parentData.clear()
                        parentData.addAll(responseData)
                        homeParentAdapter.setData(parentData, 2)
                    }
                }
                observeGetOfferLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        Log.d("TAG", "tes bool $loading")
                        rvParentHome.isGone = loading
                        shimmerLoadingSliderContainer.isGone = !loading
                    }
                }
                observeGetParentingLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        shimmerLoadingMedMomContainer.isGone = !loading
                    }
                }
                observeGetCategoryListLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        shimmerLoadingSliderContainer.isGone = !loading
                    }
                }
                observeGetDiabetLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        shimmerLoadingMedDiabetContainer.isGone = !loading
                    }
                }
                observeGetTrendingArticlesLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        shimmerLoadingArticleContainer.isGone = !loading
                    }
                }
            }
        }

    }

}