package com.example.FillThePlate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class RequestStatus : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.request_status, container, false)

        // Find the "Mark as Completed" button in the layout
        val markAsCompletedButton: Button = view.findViewById(R.id.btnComplete)

        // Set up the button click listener
        markAsCompletedButton.setOnClickListener {
            // Show a toast message when the button is clicked
            Toast.makeText(requireContext(), "Marked as completed!", Toast.LENGTH_SHORT).show()
        }

        // Return the inflated view after setting up the button click listener
        return view
    }
}
