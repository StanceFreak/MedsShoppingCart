package com.example.testing.views.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.testing.R
import com.example.testing.databinding.ActivityNavigationBinding
import com.example.testing.views.home.HomeFragment

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setupBottomNav()
        setContentView(binding.root)
    }

    private fun setupBottomNav() {
        binding.navBottomNav.apply {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.botnav_home -> {
                        loadFragment(HomeFragment())
                        true
                    }
                    else -> {
                        loadFragment(HomeFragment())
                        true
                    }
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

}