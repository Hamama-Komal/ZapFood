package com.cal.foodorderingapp.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cal.foodorderingapp.activities.DetailsActivity
import com.cal.foodorderingapp.databinding.MenuItemBinding
import com.cal.foodorderingapp.models.MenuItemModel


class MenuAdapter(
    private val menuItems: List<MenuItemModel>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }
            }
        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            val uriString = menuItem.foodImage
            val uri = Uri.parse(uriString)
            binding.apply {
                menuName.text = menuItem.foodName
                menuPrice.text = menuItem.foodPrice
                // Toast.makeText(requireContext, uri.toString(), Toast.LENGTH_SHORT).show()
                Glide.with(requireContext).load(uri.toString()).into(menuImage)

            }

        }
    }

    private fun openDetailActivity(position: Int) {
        val menuItem = menuItems[position]
        val intent= Intent(requireContext, DetailsActivity::class.java).apply {
            putExtra("menuName", menuItem.foodName)
            putExtra("menuPrice", menuItem.foodPrice)
            putExtra("menuImage", menuItem.foodImage)
            putExtra("menuDescription", menuItem.foodDescription)
            putExtra("menuIngredients", menuItem.foodIngredient)
        }
        requireContext.startActivity(intent)
    }

}

