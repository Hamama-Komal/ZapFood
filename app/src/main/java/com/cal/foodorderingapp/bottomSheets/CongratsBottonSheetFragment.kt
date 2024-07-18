package com.cal.foodorderingapp.bottomSheets

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cal.foodorderingapp.R
import com.cal.foodorderingapp.activities.MainActivity
import com.cal.foodorderingapp.databinding.FragmentCongratsBottonSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CongratsBottonSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCongratsBottonSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCongratsBottonSheetBinding.inflate(inflater, container, false)

        binding.goHomeButton.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        return binding.root
    }


}