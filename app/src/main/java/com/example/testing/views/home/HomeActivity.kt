package com.example.testing.views.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.testing.R
import com.example.testing.data.api.factory.ApiViewModelFactory
import com.example.testing.data.api.network.ApiClient
import com.example.testing.data.api.network.ApiHelper
import com.example.testing.databinding.ActivityHomeBinding
import com.example.testing.util.Status

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }


}