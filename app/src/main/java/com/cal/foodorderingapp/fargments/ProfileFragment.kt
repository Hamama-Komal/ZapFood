package com.cal.foodorderingapp.fargments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cal.foodorderingapp.databinding.FragmentProfileBinding
import com.cal.foodorderingapp.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        // by default edit fields are disable
        binding.apply {
            edtName.isEnabled = false
            edtEmail.isEnabled = false
            edtAddress.isEnabled = false
            edtPhone.isEnabled = false
        }
        setUserData()

        // Edit Information
        binding.editBtn.setOnClickListener {
            binding.apply {
                edtName.isEnabled = !edtName.isEnabled
                edtEmail.isEnabled = !edtEmail.isEnabled
                edtPhone.isEnabled = !edtPhone.isEnabled
                edtAddress.isEnabled = !edtAddress.isEnabled

                if(edtName.isEnabled){
                    edtName.requestFocus()
                }
            }
        }

        // Save Information Button
        binding.saveButton.setOnClickListener {

            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()

            updateUserData(name, email, address, phone)
        }


        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            val userData = hashMapOf(
                "name" to name, "address" to address, "email" to email, "phone" to phone
            )

            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.edtName.setText(userProfile.name)
                            binding.edtEmail.setText(userProfile.email)
                            binding.edtPhone.setText(userProfile.phone)
                            binding.edtAddress.setText(userProfile.address)

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