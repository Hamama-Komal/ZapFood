package com.cal.foodorderingapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.bottomSheets.NavigationBottomSheetFragment
import com.cal.foodorderingapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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

        // Notification Button
        binding.notificationButton.setOnClickListener {
            val bottomSheetDialog = NavigationBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }

        // Fragment navigation
        var NavController = findNavController(R.id.fragmentContainerView)
        var bottonNavController = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottonNavController.setupWithNavController(NavController)
    }
}