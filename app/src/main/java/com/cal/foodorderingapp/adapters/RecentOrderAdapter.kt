package com.cal.foodorderingapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cal.foodorderingapp.databinding.RecentOrderItemBinding

class RecentOrderAdapter(
    private var context: Context,
    private var foodNameList: ArrayList<String>,
    private var foodPriceList: ArrayList<String>,
    private var foodImageList: ArrayList<String>,
    private var foodQuantityList: ArrayList<Int>) : RecyclerView.Adapter<RecentOrderAdapter.RecentOrderViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentOrderAdapter.RecentOrderViewHolder {
        val binding = RecentOrderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecentOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentOrderViewHolder, position: Int){
        holder.bind(position)
    }

    inner class RecentOrderViewHolder (private val binding: RecentOrderItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                foodName.text = foodNameList[position]
                foodPrice.text = foodPriceList[position]
                foodQuantity.text = foodQuantityList[position].toString()
                val uriString = foodImageList[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(binding.historyImage)
2
            }
        }

    }


    override fun getItemCount(): Int {
        return foodNameList.size
    }
}