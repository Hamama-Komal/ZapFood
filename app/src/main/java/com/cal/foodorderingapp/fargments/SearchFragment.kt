package com.cal.foodorderingapp.fargments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cal.foodorderingapp.adapters.MenuAdapter
import com.cal.foodorderingapp.databinding.FragmentSearchBinding
import com.cal.foodorderingapp.models.MenuItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val allMenuItems = mutableListOf<MenuItemModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // For Search Query
        setUpSearchView()

        // Retrieve Menu Item From Database
        retrieveMenuItem()

        return binding.root
    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItemModel::class.java)
                    menuItem?.let {
                        allMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showAllMenu() {
        val filteredMenu = ArrayList(allMenuItems)
        setAdapter(filteredMenu)

    }

    private fun setAdapter(filteredMenu: List<MenuItemModel>) {
        adapter = MenuAdapter(filteredMenu, requireContext())
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.adapter = adapter
    }


    private fun setUpSearchView() {
       binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
           override fun onQueryTextSubmit(query: String): Boolean {
               filterMenuItems(query)
               return true
           }

           override fun onQueryTextChange(newText: String): Boolean {
               filterMenuItems(newText)
               return true
           }

       })
    }

    private fun filterMenuItems(query: String) {

        val filteredMenuItem = allMenuItems.filter {
            it.foodName?.contains(query, ignoreCase = true) == true
        }
        setAdapter(filteredMenuItem)
        adapter.notifyDataSetChanged()
    }

}