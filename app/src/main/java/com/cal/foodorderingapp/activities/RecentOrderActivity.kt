package com.cal.foodorderingapp.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.adapters.RecentOrderAdapter
import com.cal.foodorderingapp.databinding.ActivityRecentOrderBinding
import com.cal.foodorderingapp.models.OrderDetailsModel

@Suppress("NAME_SHADOWING")
class RecentOrderActivity : AppCompatActivity() {

    private val binding: ActivityRecentOrderBinding by lazy {
        ActivityRecentOrderBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodNames: ArrayList<String>
    private lateinit var allFoodImages: ArrayList<String>
    private lateinit var allFoodPrices: ArrayList<String>
    private lateinit var allFoodQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Back Button
        binding.backBtn.setOnClickListener {
            finish()
        }


        // Set Recycler View
        val recentOrderItem = intent.getSerializableExtra("RecentBuyOrderItem") as  ArrayList<OrderDetailsModel>
        recentOrderItem.let { orderDetails ->
            if(orderDetails.isNotEmpty()){
                val recentOrderItem = orderDetails[0]
                allFoodNames = recentOrderItem.foodNames as ArrayList<String>
                allFoodPrices = recentOrderItem.foodPrices as ArrayList<String>
                allFoodImages = recentOrderItem.foodImages as ArrayList<String>
                allFoodQuantities = recentOrderItem.foodQuantities as ArrayList<Int>
            }
            Log.d("recentOrderItem", "recentOrderItem data: ${allFoodNames}")
            setAdapter()
        }
    }

    private fun setAdapter() {
        val adapter = RecentOrderAdapter(this, allFoodNames, allFoodPrices, allFoodImages, allFoodQuantities)
        binding.recentOrderRecycler.layoutManager = LinearLayoutManager(this@RecentOrderActivity)
        binding.recentOrderRecycler.adapter = adapter
    }
}