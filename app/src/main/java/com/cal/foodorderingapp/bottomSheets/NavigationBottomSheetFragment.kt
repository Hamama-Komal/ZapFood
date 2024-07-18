package com.cal.foodorderingapp.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.adapters.NotificationAdapter
import com.cal.foodorderingapp.databinding.FragmentMenuBottomSheetBinding
import com.cal.foodorderingapp.databinding.FragmentNavigationBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NavigationBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNavigationBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentNavigationBottomSheetBinding.inflate(inflater, container, false)

        val notification = listOf("Your order has been Canceled Successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val notificationImg = listOf(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)

        val adapter = NotificationAdapter(ArrayList(notification), ArrayList(notificationImg))

        binding.notificationRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecycler.adapter = adapter



        return binding.root
    }


}