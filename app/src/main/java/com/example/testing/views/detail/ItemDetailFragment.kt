package com.example.testing.views.detail

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.navigation.Navigation
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.response.CartList
import com.example.testing.databinding.FragmentItemDetailBinding
import com.example.testing.utils.CustomTypefaceSpan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.*

//@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private lateinit var binding: FragmentItemDetailBinding
    private val viewModel: ItemDetailViewModel by inject()
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupApiCall()
        observeData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupToolbar() {
        binding.apply {
            itemDetailToolbar.setNavigationIcon(R.drawable.ic_back)
            (activity as AppCompatActivity).apply {
                setSupportActionBar(itemDetailToolbar)
                title = ""
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                itemDetailToolbar.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun setupApiCall() {
        db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
        ref = db.getReference("users")
        auth = FirebaseAuth.getInstance()
        val slugData = arguments?.getString("slug")
        viewModel.apply {
            if (slugData != null) {
                getMedicineById(slugData)
            }
        }
    }

    private fun observeData() {
        binding.apply {
            viewModel.apply {
                observeGetItemDetailSuccess().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { response ->
                        val font1 = ResourcesCompat.getFont(requireContext(), R.font.barlow_bold)
                        val font2 = ResourcesCompat.getFont(requireContext(), R.font.barlow_medium)
                        val localeID =  Locale("in", "ID")
                        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                        val productPrice = numberFormat.format(response.minPrice)
                            .replace(",00", "")
                        val productQty = response.sellingUnit.replace("Per ", "/")
                        val ss = SpannableStringBuilder(productPrice+productQty)
                        ss.setSpan(CustomTypefaceSpan("", font1!!), 0, productPrice.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        ss.setSpan(CustomTypefaceSpan("", font2!!), productPrice.length, productPrice.length + productQty?.length!!, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        Picasso.get()
                            .load(response.thumbnailUrl)
                            .placeholder(R.drawable.ic_image_placeholder)
                            .into(binding.itemDetailThumbnail)
                        getUserData().observe(viewLifecycleOwner) {uid ->
                            binding.apply {
                                val data = CartList(
                                    slug = response.canonSlug,
                                    name = response.name,
                                    thumbnailUrl = response.thumbnailUrl,
                                    minPrice = response.minPrice,
                                    quantity = 1
                                )
                                if (response.name.contains(" - ")) {
                                    itemDetailTitle.text = response.name
                                        .substring(0, response.name.indexOf(" - "))
                                    itemDetailName.text = response.name
                                        .substring(0, response.name.indexOf(" - "))
                                } else {
                                    itemDetailTitle.text = response.name
                                    itemDetailName.text = response.name
                                }
                                ref.child(uid).child("fav").child(response.canonSlug).addValueEventListener(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                tgLikeDetailItem.isChecked = true
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("valListenerCancelled", "loadPost:onCancelled", error.toException())
                                        }

                                    })
                                tgLikeDetailItem.setOnCheckedChangeListener { _, isChecked ->
                                    if (isChecked) {
                                        ref.child(uid).child("fav").child(response.canonSlug).setValue(response)
                                            .addOnCompleteListener { t ->
                                                if (t.isSuccessful) {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Berhasil menambahkan ke wishlist!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Gagal menambahkan ke wishlist!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        ref.child(uid).child("fav").child(response.canonSlug).removeValue()
                                            .addOnCompleteListener { t ->
                                                if (t.isSuccessful) {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Berhasil menghapus barang dari wishlist!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Gagal menghapus barang dari wishlist!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    }
                                }
                                itemDetailPrice.text = ss
                                itemDetailCategory.text = response.categories[0].name
                                itemDetailDesc.text = response.description
                                itemDetailGeneralIndication.text = response.generalIndication
                                itemDetailComposition.text = response.composition
                                itemDetailDosage.text = response.dosage
                                itemDetailHowToUse.text = response.howToUse
                                itemDetailWarning.text = response.warning
                                itemDetailSideEffects.text = response.sideEffects
                                itemDetailManufacturer.text = response.manufacturerName
                                itemDetailBpomNumber.text = response.bpomNumber
                                itemDetailBtnAddToCart.setOnClickListener {
                                    ref.child(uid).child("cart").child(response.canonSlug).setValue(data)
                                        .addOnCompleteListener { t ->
                                            if (t.isSuccessful) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Berhasil menambahkan ke keranjang!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Gagal menambahkan ke keranjang!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.detail_to_shopping) }
                                }
                            }
                        }
                    }
                }
                observeGetItemDetailLoading().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { loading ->
                        shimmerPlaceholderContainer.isGone = !loading
                        shimmerLoadingTitleDetail.isGone = !loading
                        itemDetailContent.isGone = loading
                        itemDetailTitle.isGone = loading
                        itemDetailAddToCartContainer.isGone = loading
                    }
                }
                observeGetItemDetailError().observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { error ->
                        if (error.second != null) {
                            shimmerPlaceholderContainer.isGone = !error.first
                            shimmerLoadingTitleDetail.isGone = !error.first
                            itemDetailContent.isGone = error.first
                            itemDetailTitle.isGone = error.first
                            itemDetailTitle.text = ""
                            itemDetailAddToCartContainer.isGone = error.first
                            Toast.makeText(requireContext(), error.second, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

}