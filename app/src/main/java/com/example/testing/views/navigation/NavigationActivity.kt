package com.example.testing.views.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.testing.R
import com.example.testing.databinding.ActivityNavigationBinding
import com.example.testing.views.cart.ShoppingCartFragment
import com.example.testing.views.detail.ItemDetailFragment
import com.example.testing.views.home.HomeFragment
import com.example.testing.views.wishlist.WishlistFragment

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setupNavigation()
        setContentView(binding.root)
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        binding.navBottomNav.setupWithNavController(navController)
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.wishlistFragment,
                R.id.profileFragment,
            )
        )
        navController.addOnDestinationChangedListener { _: NavController, destination: NavDestination, _: Bundle? ->
            binding.navBottomNav.isVisible = appBarConfig.topLevelDestinations.contains(destination.id)
        }
    }

//    private fun setupBottomNav() {
//        binding.navBottomNav.apply {
//            setupWithNavController()
//            setOnItemSelectedListener {
//                when (it.itemId) {
//                    R.id.botnav_home -> {
//                        loadFragment(HomeFragment())
//                        true
//                    }
//                    R.id.botnav_wishlist -> {
//                        loadFragment(HomeFragment())
//                        true
//                    }
//                    R.id.botnav_profile -> {
//                        loadFragment(HomeFragment())
//                        true
//                    }
//                    else -> {
//                        loadFragment(HomeFragment())
//                        true
//                    }
//                }
//            }
//        }
//    }

//    private fun loadFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, fragment)
//            .addToBackStack(fragment::class.java.simpleName)
//            .commit()
//    }

}