package com.cal.foodorderingapp.fargments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cal.foodorderingapp.activities.PayOutActivity
import com.cal.foodorderingapp.adapters.CartAdapter
import com.cal.foodorderingapp.databinding.FragmentCartBinding
import com.cal.foodorderingapp.models.CartItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var foodName: MutableList<String>
    private lateinit var foodPrice: MutableList<String>
    private lateinit var foodImageUri: MutableList<String>
    private lateinit var foodDescription: MutableList<String>
    private lateinit var foodIngredient: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)

        // Initialization
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""

        retrieveCartItems()


        // Proceed Button
        binding.buttonProceed.setOnClickListener {
            getOrderItemsDetails()
        }


        return binding.root
    }

    private fun getOrderItemsDetails() {
        val orderIdRef: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItem")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImageUri = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()
        val foodQuantity = cartAdapter.getUpdatedItemQuantities()
        Log.d("Quantity","$foodQuantity")

        orderIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (foodsnapshot in snapshot.children) {
                    val orderItems = foodsnapshot.getValue(CartItemModel::class.java)
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodIngredients?.let { foodIngredient.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImageUri.add(it) }
                    orderItems?.foodQuantity?.let { foodQuantity.add(it) }
                }
                Log.d("Quantity","2)......$foodQuantity")
                orderNow(foodName, foodPrice, foodDescription, foodIngredient, foodImageUri, foodQuantity)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodImageUri: MutableList<String>,
        foodQuantity: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("FoodName", foodName as ArrayList<String>)
            intent.putExtra("FoodPrice", foodPrice as ArrayList<String>)
            intent.putExtra("FoodImage", foodImageUri as ArrayList<String>)
            intent.putExtra("FoodIngredients", foodIngredient as ArrayList<String>)
            intent.putExtra("FoodDescription", foodDescription as ArrayList<String>)
            intent.putExtra("FoodQuantity", foodQuantity as ArrayList<Int>)
           // Log.d("Quantity","$foodQuantity")
            startActivity(intent)


        }
    }

    private fun retrieveCartItems() {
        val foodReference: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItem")

        foodName = mutableListOf()
        foodPrice = mutableListOf()
        foodImageUri = mutableListOf()
        foodDescription = mutableListOf()
        foodIngredient = mutableListOf()
        quantity = mutableListOf()

        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnashot in snapshot.children) {
                    val cartItems = foodSnashot.getValue(CartItemModel::class.java)

                    cartItems?.foodName?.let { foodName.add(it) }
                    cartItems?.foodPrice?.let { foodPrice.add(it) }
                    cartItems?.foodImage?.let { foodImageUri.add(it) }
                    cartItems?.foodDescription?.let { foodDescription.add(it) }
                    cartItems?.foodIngredients?.let { foodIngredient.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setAdapter() {
        cartAdapter = CartAdapter(
            requireContext(),
            foodName,
            foodPrice,
            foodImageUri,
            foodDescription,
            foodIngredient,
            quantity
        )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = cartAdapter
    }

}