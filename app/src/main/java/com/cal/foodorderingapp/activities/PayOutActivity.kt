package com.cal.foodorderingapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.bottomSheets.CongratsBottonSheetFragment
import com.cal.foodorderingapp.databinding.ActivityPayOutBinding
import com.cal.foodorderingapp.models.OrderDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {

    private val binding: ActivityPayOutBinding by lazy {
        ActivityPayOutBinding.inflate(layoutInflater, null, false)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodName: ArrayList<String>
    private lateinit var foodPrice: ArrayList<String>
    private lateinit var foodImage: ArrayList<String>
    private lateinit var foodQuantity: ArrayList<Int>
    private lateinit var foodIngredients: ArrayList<String>
    private lateinit var foodDescription: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        userId = auth.currentUser?.uid ?: ""

        setUserData()

        // Get Details From Firebase
        foodName = intent.getStringArrayListExtra("FoodName") as ArrayList<String>
        foodPrice = intent.getStringArrayListExtra("FoodPrice") as ArrayList<String>
        foodImage = intent.getStringArrayListExtra("FoodImage") as ArrayList<String>
        foodIngredients = intent.getStringArrayListExtra("FoodIngredients") as ArrayList<String>
        foodDescription = intent.getStringArrayListExtra("FoodDescription") as ArrayList<String>
        foodQuantity = intent.getIntegerArrayListExtra("FoodQuantity") as ArrayList<Int>

        totalAmount = "Rs " + calculateToatalAmount().toString()
        binding.edtPrice.isEnabled = false
        binding.edtPrice.text = totalAmount



        binding.placeOrderButton.setOnClickListener {
            // get User details
            name = binding.edtName.text.toString().trim()
            email = binding.edtEmail.text.toString().trim()
            phone = binding.edtPhone.text.toString().trim()
            address = binding.edtAddress.text.toString().trim()

            if (name.isBlank() && phone.isBlank() && address.isBlank()) {
                Toast.makeText(this@PayOutActivity, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                placeOrder()
            }

        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("orderDetails").push().key
        val orderDetails = OrderDetailsModel(
            userId,
            name,
            foodName,
            foodImage,
            foodPrice,
            foodQuantity,
            address,
            totalAmount,
            phone,
            time,
            itemPushKey,
            b = false,
            b1 = false
        )
        val orderRef = databaseReference.child("orderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            // Congrats Bottom Sheet
            val bottomDialog = CongratsBottonSheetFragment()
            bottomDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
            
        }.addOnFailureListener { e ->
            Toast.makeText(this@PayOutActivity, "$e", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addOrderToHistory(orderDetails: OrderDetailsModel){
        databaseReference.child("user").child(userId).child("buyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails)
            .addOnSuccessListener {
                Toast.makeText(this@PayOutActivity, "Order Done", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@PayOutActivity, "$e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeItemFromCart() {
        var cartItemsReference = databaseReference.child("user").child(userId).child("cartItem")
        cartItemsReference.removeValue()
    }


    private fun calculateToatalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodName.size) {
            val price = foodPrice[i]
            val priceIntValue = price.toInt()
            val quantity = foodQuantity[i]
            totalAmount += priceIntValue * quantity
        }

        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: ""
                        val email = snapshot.child("email").getValue(String::class.java) ?: ""
                        val address = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phone = snapshot.child("phone").getValue(String::class.java) ?: ""

                        binding.apply {
                            edtName.setText(name)
                            edtEmail.setText(email)
                            edtAddress.setText(address)
                            edtPhone.setText(phone)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }
}