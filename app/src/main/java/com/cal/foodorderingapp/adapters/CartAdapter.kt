package com.cal.foodorderingapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cal.foodorderingapp.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private val cartItemImages: MutableList<String>,
    private var cartDescription: MutableList<String>,
    private var cartIngredient: MutableList<String>,
    private var cartQuantity: MutableList<Int>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartItems.size

        itemQuantity = IntArray(cartItemNumber){1}
        cartItemReference = database.reference.child("user").child(userId).child("cartItem")
    }

    companion object{
        private lateinit var itemQuantity: IntArray
        private lateinit var cartItemReference: DatabaseReference

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                cartItemName.text = cartItems[position]
                cartPrice.text = cartItemPrices[position]
                cartItemQuantity.text = quantity.toString()


                // Set Image
                val uriString = cartItemImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri.toString()).into(cartImage)

                btnMinus.setOnClickListener {
                    decrementQuantity(position)
                }

                btnPlus.setOnClickListener {
                    incrementQuantity(position)
                }

                btnDel.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }

                }
            }
        }

        private fun decrementQuantity(position: Int) {
            if (itemQuantity[position] > 1) {
                itemQuantity[position]--
                cartQuantity[position] = itemQuantity[position]
                binding.cartItemQuantity.text = itemQuantity[position].toString()
            }
        }

        private fun incrementQuantity(position: Int) {
            if (itemQuantity[position] < 10) {
                itemQuantity[position]++
                cartQuantity[position] = itemQuantity[position]
                binding.cartItemQuantity.text = itemQuantity[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            val getPosition = position
            getUniqueKeyPostion(getPosition){ uniKey ->
                removeItem(position, uniKey)
            }
        }



    }

    private fun removeItem(position: Int, uniKey: String) {
        cartItemReference.child(uniKey).removeValue().addOnSuccessListener {
            cartItems.removeAt(position)
            cartItemImages.removeAt(position)
            cartItemPrices.removeAt(position)
            cartDescription.removeAt(position)
            cartIngredient.removeAt(position)
            cartQuantity.removeAt(position)

            // Upgrade Quantity
            itemQuantity = itemQuantity.filterIndexed { index, i -> index != position }.toIntArray()
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
        }.addOnFailureListener { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUniqueKeyPostion(getPosition: Int, onComplete: (String) -> Unit ) {
        cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey: String? = null
                snapshot.children.forEachIndexed{index, dataSnapshot ->
                    if(index == getPosition){
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                uniqueKey?.let { onComplete(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getUpdatedItemQuantities(): MutableList<Int> {
        return cartQuantity.toMutableList()
    }



}