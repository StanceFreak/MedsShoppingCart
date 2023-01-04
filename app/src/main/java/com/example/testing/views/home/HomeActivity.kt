package com.example.testing.views.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testing.R
import com.example.testing.data.api.factory.ApiViewModelFactory
import com.example.testing.data.api.network.Api
import com.example.testing.data.api.network.ApiClient
import com.example.testing.data.api.network.ApiHelper
import com.example.testing.databinding.ActivityHomeBinding
import com.example.testing.util.Status
import com.example.testing.views.adapter.MedicineListAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var medicineListAdapter: MedicineListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setupAdapter()
        setupApiCall()
        setContentView(binding.root)
    }

    private fun setupAdapter() {
        medicineListAdapter = MedicineListAdapter()
        binding.rvHome.apply {
            layoutManager = GridLayoutManager(
                this@HomeActivity,
                2,
            )
            adapter = medicineListAdapter
        }
    }

    private fun setupApiCall() {

        viewModel = ViewModelProvider(
            this,
            ApiViewModelFactory(ApiHelper(ApiClient.instance))
        ).get(HomeViewModel::class.java)

        viewModel.getPenawaranSpecialMedicine().observe(this) {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        binding.shimmerPlaceholderContainer.visibility = View.GONE
                        binding.rvHome.visibility = View.VISIBLE
                        resource.data?.let { response ->
                            medicineListAdapter.setData(response.result)
                        }
                    }
                    Status.ERROR -> {
                        binding.shimmerPlaceholderContainer.stopShimmer()
                        binding.shimmerPlaceholderContainer.visibility = View.INVISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.shimmerPlaceholderContainer.visibility = View.VISIBLE
                    }
                }
            }
        }

    }
}