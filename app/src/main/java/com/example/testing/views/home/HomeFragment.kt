package com.example.testing.views.home

import android.os.Bundle
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
    private lateinit var homeSliderPromoAdapter: HomeSliderPromoAdapter

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
        homeSliderPromoAdapter = HomeSliderPromoAdapter()
        binding.apply {
            svSliderHome.apply {
                autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
                setIndicatorEnabled(true)
                indicatorSelectedColor = resources.getColor(R.color.dark_gray)
                indicatorUnselectedColor = resources.getColor(R.color.french_gray)
                setSliderAdapter(homeSliderPromoAdapter)
                scrollTimeInSec = 3
                isAutoCycle = true
                startAutoCycle()
            }
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
                homeSliderPromoAdapter.notifyDataSetChanged()
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
                        homeSliderPromoAdapter.setData(response.medicineList)
                        svSliderHome.setInfiniteAdapterEnabled(true)
                        ivForwardPromoHome.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("pathData", "penawaran-special")
                            bundle.putString("labelData", "Penawaran Spesial")
                            findNavController().navigate(R.id.home_to_see_all, bundle)
                        }
                    }
                }
                observeGetParentingSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        responseData.add(HomeParentResponse("Kebutuhan Ibu & Anak", "ibu-dan-anak", 1,  response.medicineList, null))
                        parentData.clear()
                        parentData.addAll(responseData)
                        homeParentAdapter.setData(parentData)
                    }
                }
                observeGetDiabetSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        responseData.add(HomeParentResponse("Perawatan Diabetes", "diabetes-medicine", 1,  response.medicineList, null))
                        parentData.clear()
                        parentData.addAll(responseData)
                        homeParentAdapter.setData(parentData)
                    }
                }
                observeGetTrendingArticlesSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        responseData.add(HomeParentResponse("Artikel Terkini", null, 2, null, response))
                        parentData.clear()
                        parentData.addAll(responseData)
                        homeParentAdapter.setData(parentData)
                    }
                }
                observeGetOfferLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        if (loading) {
                            tvTitlePromoHome.isGone = true
                            ivForwardPromoHome.isGone = true
                            svSliderHome.isGone = true
                            shimmerLoadingSliderContainer.isGone = false
                        }
                        else {
                            tvTitlePromoHome.isGone = false
                            ivForwardPromoHome.isGone = false
                            svSliderHome.isGone = false
                            shimmerLoadingSliderContainer.isGone = true
                        }
                    }
                }
                observeGetParentingLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        rvParentHome.isGone = loading
                        shimmerLoadingMedMomContainer.isGone = !loading
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