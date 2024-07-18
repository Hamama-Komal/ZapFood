package com.cal.foodorderingapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {

    private val binding: ActivityLocationBinding by lazy {
        ActivityLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val location_list = arrayOf("Bhakkar", "Darya Khan", "Kotla Jam", "Khawaar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, location_list)
        val autoComplete = binding.listLocation
        autoComplete.setAdapter(adapter)


    }
}