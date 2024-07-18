package com.cal.foodorderingapp.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.databinding.ActivityDetailsBinding
import com.cal.foodorderingapp.models.CartItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private val binding : ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }

    private var foodName: String? = null
    private var foodImageUrl: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        foodName = intent.getStringExtra("menuName")
        foodPrice = intent.getStringExtra("menuPrice")
        foodImageUrl = intent.getStringExtra("menuImage")
        foodDescription = intent.getStringExtra("menuDescription")
        foodIngredients = intent.getStringExtra("menuIngredients")

        val uri = Uri.parse(foodImageUrl)


        binding.txtFoodName.text = foodName
        binding.textDes.text = foodDescription
        binding.textIng.text = foodIngredients
        Glide.with(this@DetailsActivity).load(uri.toString()).into(binding.foodImage)

        binding.backButton.setOnClickListener {
            finish()
        }

        // Add To cart Button
        binding.addToCartBtn.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        // Create cart Item object
        val cartItem = CartItemModel(foodName, foodPrice, foodDescription, foodImageUrl, foodIngredients, 1)
        // save data to firebase
        database.child("user").child(userId).child("cartItem").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this,"Added to Cart", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
    }
}