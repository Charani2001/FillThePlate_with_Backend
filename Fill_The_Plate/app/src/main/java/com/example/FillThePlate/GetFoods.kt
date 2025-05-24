package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.FillThePlate.R

class GetFoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.get_foods, container, false)

        // Find the button in the layout
        val getItButton: Button = view.findViewById(R.id.getIt)  // Use the correct ID of your button

        // Set up the button click listener
        getItButton.setOnClickListener {
            // Show the toast message when the button is clicked
            Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
