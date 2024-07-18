package com.cal.foodorderingapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cal.foodorderingapp.databinding.HistoryItemBinding


class HistoryAdapter (private val historyItems: MutableList<String>, private val historyImages: MutableList<String>, private val historyPrices: MutableList<String>, private val context: Context): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = historyItems.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                binding.historyItemName.text = historyItems[position]
                binding.historyPrice.text = historyPrices[position]
               // binding.historyImage.setImageResource(historyImages[position])
                val uri = historyImages[position]
                val uriString = Uri.parse(uri)
                Glide.with(context).load(uriString).into(binding.historyImage)

            }
        }

    }
}