package com.cal.foodorderingapp.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.adapters.MenuAdapter
import com.cal.foodorderingapp.databinding.FragmentMenuBottomSheetBinding
import com.cal.foodorderingapp.models.MenuItemModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItemModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        // Back Button
        binding.backBtn.setOnClickListener {
            dismiss()
        }

        retrieveMenuItems()


        return binding.root
    }

    private fun retrieveMenuItems() {

        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSanpshot in snapshot.children){
                    val menuItem = foodSanpshot.getValue(MenuItemModel::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun setAdapter() {
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.menuRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecycler.adapter = adapter

    }

}