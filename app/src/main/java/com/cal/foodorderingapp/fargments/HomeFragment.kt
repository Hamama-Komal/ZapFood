package com.cal.foodorderingapp.fargments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.adapters.MenuAdapter
import com.cal.foodorderingapp.adapters.PopularAdapter
import com.cal.foodorderingapp.bottomSheets.MenuBottomSheetFragment
import com.cal.foodorderingapp.databinding.FragmentHomeBinding
import com.cal.foodorderingapp.models.MenuItemModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItemModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.viewMenu.setOnClickListener {
            val bottomSheetFragment = MenuBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, "TestBottomSheet")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Image Slider
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $itemPosition"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Retrieve Data from database
        binding.progressBar.visibility = View.VISIBLE
        retrievePopularMenuItem()

    }

    private fun retrievePopularMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSanpshot in snapshot.children) {
                    val menuItem = foodSanpshot.getValue(MenuItemModel::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                randomMenuList()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun randomMenuList() {
        // create a shuffle list
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }
        setAdapter(subsetMenuItems)
    }

    private fun setAdapter(subsetMenuItems: List<MenuItemModel>) {
            val adapter = MenuAdapter(subsetMenuItems, requireContext())
            binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.popularRecyclerView.adapter = adapter
            binding.progressBar.visibility = View.INVISIBLE
    }


}