package com.example.testing.views.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.testing.R
import com.example.testing.data.api.factory.ApiViewModelFactory
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.data.api.network.Api
import com.example.testing.data.api.network.ApiClient
import com.example.testing.data.api.network.ApiHelper
import com.example.testing.databinding.ActivityItemDetailBinding
import com.example.testing.util.Status
import com.example.testing.views.cart.ShoppingCartActivity
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding
    private lateinit var viewModel: ItemDetailViewModel
    private var cartData = ArrayList<CartList>()
//    private val cartData: MutableList<CartList> = mutableListOf()

    companion object {
        const val ITEM_SLUG ="item_slug"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setupToolbar()
        setupApiCall()
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        binding.itemDetailToolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
        }
        setSupportActionBar(binding.itemDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.itemDetailToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupApiCall() {
        val slug = intent.getStringExtra(ITEM_SLUG)!!

        viewModel = ViewModelProvider(
            this,
            ApiViewModelFactory(ApiHelper(ApiClient.instance))
        ).get(ItemDetailViewModel::class.java)

        viewModel.getMedicineById(slug).observe(this) {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        binding.shimmerPlaceholderContainer.visibility = View.GONE
                        binding.itemDetailContent.visibility = View.VISIBLE
                        resource.data?.let { response ->
                            val localeID =  Locale("in", "ID")
                            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                            Picasso.get()
                                .load(response.thumbnailUrl)
                                .placeholder(R.drawable.ic_image_placeholder)
                                .into(binding.itemDetailThumbnail)
                            binding.apply {
                                if (response.name.contains(" - ")) {
                                    itemDetailTitle.text = response.name
                                        .substring(0, response.name.indexOf(" - "))
                                    itemDetailName.text = response.name
                                        .substring(0, response.name.indexOf(" - "))
                                }
                                else {
                                    itemDetailTitle.text = response.name
                                    itemDetailName.text = response.name
                                }

                                itemDetailPrice.text = numberFormat.format(response.minPrice)
                                    .replace("Rp", "Rp ")
                                    .replace(",00", "")
                                itemDetailSellingUnit.text = response.sellingUnit
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
                                    cartData = arrayListOf()
                                    val data = CartList(
                                        slug = response.canonSlug,
                                        name = response.name,
                                        thumbnailUrl = response.thumbnailUrl,
                                        minPrice = response.minPrice,
                                        quantity = 1
                                    )
                                    cartData.add(data)
                                    Log.d("testData", cartData.toString())
                                    val i = Intent(
                                        this@ItemDetailActivity,
                                        ShoppingCartActivity::class.java
                                    )
//                                    i.putExtra(ShoppingCartActivity.ITEM_SLUG, response.canonSlug)
                                    startActivity(i)
                                }
                            }

                        }
                    }
                    Status.ERROR -> {
                        binding.shimmerPlaceholderContainer.stopShimmer()
                        binding.shimmerPlaceholderContainer.visibility = View.INVISIBLE
                        binding.itemDetailContent.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.shimmerPlaceholderContainer.visibility = View.VISIBLE
                        binding.itemDetailContent.visibility = View.GONE
                    }
                }
            }
        }

    }
}