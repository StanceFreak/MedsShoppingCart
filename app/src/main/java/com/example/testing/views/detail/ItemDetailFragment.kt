package com.example.testing.views.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.databinding.ActivityItemDetailBinding
import com.example.testing.databinding.FragmentItemDetailBinding
import com.example.testing.util.Status
import com.example.testing.views.cart.ShoppingCartActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.*

class ItemDetailFragment : Fragment() {

//    companion object {
//        const val ITEM_SLUG = "item_slug"
//    }
    val argsData: ItemDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentItemDetailBinding
    private val viewModel: ItemDetailViewModel by viewModel()
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupApiCall()
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
        binding.itemDetailToolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
        }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.itemDetailToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.itemDetailToolbar.setNavigationOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.detail_to_home) }
//            (activity as AppCompatActivity).onBackPressed()
        }
    }

    private fun setupApiCall() {
        db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
        ref = db.getReference("users")
//        val slug = arguments?.getString(ItemDetailActivity.ITEM_SLUG)!!
        viewModel.getMedicineById(argsData.itemSlug).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.shimmerPlaceholderContainer.visibility = View.GONE
                        binding.itemDetailContent.visibility = View.VISIBLE
                        resource.data?.let { response ->
                            val localeID = Locale("in", "ID")
                            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                            if (response.body() != null) {
                                Picasso.get()
                                    .load(response.body()!!.thumbnailUrl)
                                    .placeholder(R.drawable.ic_image_placeholder)
                                    .into(binding.itemDetailThumbnail)
                                binding.apply {
                                    if (response.body()!!.name.contains(" - ")) {
                                        itemDetailTitle.text = response.body()!!.name
                                            .substring(0, response.body()!!.name.indexOf(" - "))
                                        itemDetailName.text = response.body()!!.name
                                            .substring(0, response.body()!!.name.indexOf(" - "))
                                    } else {
                                        itemDetailTitle.text = response.body()!!.name
                                        itemDetailName.text = response.body()!!.name
                                    }

                                    itemDetailPrice.text = numberFormat.format(response.body()!!.minPrice)
                                        .replace("Rp", "Rp ")
                                        .replace(",00", "")
                                    itemDetailSellingUnit.text = response.body()!!.sellingUnit
                                    itemDetailCategory.text = response.body()!!.categories[0].name
                                    itemDetailDesc.text = response.body()?.description
                                    itemDetailGeneralIndication.text = response.body()?.generalIndication
                                    itemDetailComposition.text = response.body()?.composition
                                    itemDetailDosage.text = response.body()?.dosage
                                    itemDetailHowToUse.text = response.body()?.howToUse
                                    itemDetailWarning.text = response.body()?.warning
                                    itemDetailSideEffects.text = response.body()?.sideEffects
                                    itemDetailManufacturer.text = response.body()?.manufacturerName
                                    itemDetailBpomNumber.text = response.body()?.bpomNumber
                                    itemDetailBtnAddToCart.setOnClickListener {
                                        val data = CartList(
                                            slug = response.body()?.canonSlug,
                                            name = response.body()?.name,
                                            thumbnailUrl = response.body()?.thumbnailUrl,
                                            minPrice = response.body()?.minPrice,
                                            quantity = 1
                                        )
                                        ref.child(response.body()?.canonSlug!!).setValue(data)
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
//                                        val i = Intent(
//                                            requireContext(),
//                                            ShoppingCartActivity::class.java
//                                        )
//                                        startActivity(i)
                                    }
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.shimmerPlaceholderContainer.stopShimmer()
                        binding.shimmerPlaceholderContainer.visibility = View.INVISIBLE
                        binding.itemDetailContent.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.shimmerPlaceholderContainer.visibility = View.VISIBLE
                        binding.itemDetailContent.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getRandomId(): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { charset.random() }.joinToString("")
    }

}