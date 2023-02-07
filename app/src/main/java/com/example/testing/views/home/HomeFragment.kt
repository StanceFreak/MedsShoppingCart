package com.example.testing.views.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testing.R
import com.example.testing.databinding.ActivityHomeBinding
import com.example.testing.util.Status
import com.example.testing.views.adapter.MedicineListAdapter
import com.example.testing.views.cart.ShoppingCartActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var medicineListAdapter: MedicineListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAdapter()
        setupApiCall()
        setupViews()
        setupToolbar()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupAdapter() {
        medicineListAdapter = MedicineListAdapter()
        binding.rvHome.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                2,
            )
            adapter = medicineListAdapter
        }
    }

    private fun setupToolbar() {
        binding.homeToolbar.inflateMenu(R.menu.menu_home)
        binding.homeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.home_cart -> {
                    val i = Intent(requireContext(), ShoppingCartActivity::class.java)
                    startActivity(i)
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupViews() {
        binding.homeSwipeRefresh.setOnRefreshListener {
            binding.homeSwipeRefresh.isRefreshing = false
            setupApiCall()
            medicineListAdapter.notifyDataSetChanged()
        }
    }

    private fun setupApiCall() {
        viewModel.getPenawaranSpecialMedicine().observe(viewLifecycleOwner) {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        binding.shimmerPlaceholderContainer.visibility = View.GONE
                        binding.rvHome.visibility = View.VISIBLE
                        resource.data?.let { response ->
                            response.body()
                                ?.let { it1 -> medicineListAdapter.setData(it1.medicineList) }
                        }
                    }
                    Status.ERROR -> {
                        binding.shimmerPlaceholderContainer.stopShimmer()
                        binding.shimmerPlaceholderContainer.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.shimmerPlaceholderContainer.visibility = View.VISIBLE
                        binding.rvHome.visibility = View.GONE
                    }
                }
            }
        }

    }

}