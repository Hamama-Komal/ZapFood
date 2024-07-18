package com.cal.foodorderingapp.fargments

import android.app.appsearch.AppSearchSchema.StringPropertyConfig
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.activities.RecentOrderActivity
import com.cal.foodorderingapp.adapters.HistoryAdapter
import com.cal.foodorderingapp.databinding.FragmentHistoryBinding
import com.cal.foodorderingapp.models.OrderDetailsModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetailsModel> = mutableListOf()
    private var historyItems: MutableList<String> = mutableListOf()
    private var historyImages: MutableList<String> = mutableListOf()
    private var historyPrices: MutableList<String> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.progressBar.visibility = View.VISIBLE
        // Retrieve History Data
        retrieveBuyHistory()

        // To view Recent Items
        binding.recentBuyItem.setOnClickListener {
            seeRecentItems()
        }
        // Received Button
        binding.status.setOnClickListener {
            updateOrderStatus()
        }

        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("completedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeRecentItems() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), RecentOrderActivity::class.java)
            intent.putExtra("RecentBuyOrderItem", ArrayList(listOfOrderItem))
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""
        val buyItemReference = database.reference.child("user").child(userId).child("buyHistory")
        val sortingQuery = buyItemReference.orderByChild("currentTime")
        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetailsModel::class.java)
                    buyHistoryItem?.let { listOfOrderItem.add(it) }
                }
                // Log.d("HistoryFragment", "Order List: ${listOfOrderItem.size}")
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                  //   Log.d("HistoryFragment", "Order item added: $listOfOrderItem")
                    setDataOnCard()
                    setPreviousBuyItems()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setDataOnCard() {
        binding.recentBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding) {
                foodName.text = it.foodNames?.firstOrNull() ?: ""
                foodPrice.text = "Rs ${it.foodPrices?.firstOrNull()}" ?: ""
                val image = it.foodImages?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(foodImage)
                val isOrderIsAccepted = listOfOrderItem[0].orderAccepted
                if(isOrderIsAccepted){
                    statusCard.background.setTint(Color.GREEN)
                    status.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setPreviousBuyItems() {

        historyItems.clear()
        historyImages.clear()
        historyPrices.clear()

        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames?.forEach { foodName ->
                historyItems.add(foodName)
            }
            listOfOrderItem[i].foodPrices?.forEach { foodPrice ->
                historyPrices.add(foodPrice)
            }
            listOfOrderItem[i].foodImages?.forEach { foodImage ->
                historyImages.add(foodImage)
            }
        }

       // Log.d("HistoryFragment", "History List: ${historyItems.size}")
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = HistoryAdapter(
            historyItems,
            historyImages,
            historyPrices,
            requireContext()
        )
        binding.historyRecycler.adapter = historyAdapter
        binding.progressBar.visibility = View.GONE
    }

}