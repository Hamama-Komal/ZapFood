package com.cal.foodorderingapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cal.foodorderingapp.activities.DetailsActivity
import com.cal.foodorderingapp.databinding.PopularItemBinding

class PopularAdapter(
    private val items: List<String>,
    private val image: List<Int>,
    private val price: List<String>,
    private val requireContext: Context
) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            PopularItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val image = image[position]
        val price = price[position]
        holder.bind(item, image, price)

        // On Item Click
        holder.itemView.setOnClickListener {
            // Item Click Listener
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("menuName", item)
            intent.putExtra("menuPrice", price)
            intent.putExtra("menuImage", image)
            requireContext.startActivity(intent)
        }
    }

    class PopularViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, image: Int, price: String) {
            binding.popularName.text = item
            binding.popularImage.setImageResource(image)
            binding.popularPrice.text = price
        }

    }
}