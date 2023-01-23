package com.example.testing.views.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testing.R
import com.example.testing.databinding.ActivityHomeBinding
import com.example.testing.util.Status
import com.example.testing.views.adapter.MedicineListAdapter
import com.example.testing.views.cart.ShoppingCartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var medicineListAdapter: MedicineListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setupAdapter()
        setupApiCall()
        setupToolbar()
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

    private fun setupToolbar() {
        binding.homeToolbar.inflateMenu(R.menu.menu_home)
        binding.homeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.home_cart -> {
                    val i = Intent(this@HomeActivity, ShoppingCartActivity::class.java)
                    startActivity(i)
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupApiCall() {
        viewModel.getPenawaranSpecialMedicine().observe(this) {
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