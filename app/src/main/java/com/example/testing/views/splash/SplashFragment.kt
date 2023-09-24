package com.example.testing.views.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testing.R
import com.example.testing.databinding.FragmentSplashBinding
import com.example.testing.views.navigation.NavigationActivity
import org.koin.android.ext.android.inject

@Suppress("DEPRECATION")
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
//        Handler(Looper.myLooper()!!).postDelayed({
//            lifecycleScope.launchWhenResumed {
//                findNavController().navigate(R.id.splash_to_home)
//            }
//        }, 3000)
        viewModel.getLoginState().observe(viewLifecycleOwner) { isLogin ->
            if (isLogin == true) {
                Handler(Looper.myLooper()!!).postDelayed({
                    lifecycleScope.launchWhenResumed {
                        findNavController().navigate(R.id.splash_to_home)
                    }
                }, 3000)
            }
            else {
                Handler(Looper.myLooper()!!).postDelayed({
                    lifecycleScope.launchWhenResumed {
                        findNavController().navigate(R.id.splash_to_login)
                    }
                }, 3000)
            }
        }
    }
}