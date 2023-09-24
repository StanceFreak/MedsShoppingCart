package com.example.testing.views.wishlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.response.CartList
import com.example.testing.data.api.model.response.MedicineList
import com.example.testing.databinding.FragmentWishlistBinding
import com.example.testing.views.adapter.WishlistAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.Locale

class WishlistFragment : Fragment() {

    private lateinit var binding: FragmentWishlistBinding
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private val viewModel: WishlistViewModel by inject()
    private val dataList = ArrayList<MedicineList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupAdapter()
        setupViews()
    }

    private fun setupToolbar() {
        binding.apply {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbarWishlist)
                title = ""
            }
        }
    }

    private fun setupAdapter() {
        wishlistAdapter = WishlistAdapter()
        binding.rvListWishlist.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = wishlistAdapter
        }
    }

    private fun setupViews() {
//        val dataList = ArrayList<MedicineList>()
        db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
        ref = db.getReference("users")
        binding.apply {
            viewModel.getUserData().observe(viewLifecycleOwner) { uid ->
                ref.child(uid).child("fav").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            val data = ds.getValue<MedicineList>()
                            if (data != null) {
                                dataList.add(data)
                            }
                        }
                        Log.d("test wish", dataList.toString())
                        wishlistAdapter.setData(dataList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("valListenerCancelled", "loadPost:onCancelled", error.toException())
                    }

                })
            }
        }
    }

}