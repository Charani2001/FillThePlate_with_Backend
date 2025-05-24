package com.example.FillThePlate

import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.preference.PreferenceManager

import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException


import android.os.Bundle

import androidx.fragment.app.Fragment

import okhttp3.Callback
import org.json.JSONObject

class DonateFoods : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.donate_foods, container, false)

        val fullName = view.findViewById<EditText>(R.id.fullName)
        val contactNumber = view.findViewById<EditText>(R.id.contactNumber)
        val emailAddress = view.findViewById<EditText>(R.id.emailAddress)
        val address = view.findViewById<EditText>(R.id.address)
        val foodCategory = view.findViewById<Spinner>(R.id.foodCategory)
        val foodItemName = view.findViewById<EditText>(R.id.foodItemName)
        val servings = view.findViewById<EditText>(R.id.savings)
        val pickUpLocation = view.findViewById<EditText>(R.id.pickupLocation)
        val submitButton = view.findViewById<Button>(R.id.submitButton)

        // Set up the button click listener
        submitButton.setOnClickListener {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val baseUrl = sharedPref.getString("backend_url", "http://10.0.2.2:5000") // default fallback

            val url = "$baseUrl/api/donations/post"

            val json = """
{
  "fullName": "${fullName.text}",
  "contactNumber": "${contactNumber.text}",
  "email": "${emailAddress.text}",
  "address": "${address.text}",
  "foodType": "${foodCategory.selectedItem}",
  "foodName": "${foodItemName.text}",
  "servings": "${servings.text}",
  "location": "${pickUpLocation.text}"
}
""".trimIndent()

            val client = OkHttpClient()
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Failed to connect: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {

                    if (response.isSuccessful) {


                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Donation adding successful", Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        requireActivity().runOnUiThread {
                            val body = response.body?.string()

                            val messageBody = JSONObject(body)

                            val message = messageBody.optString("message","Unknown error")

                         Toast.makeText(requireContext(), "Donation adding failed: $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            })


        }

        return view
    }
}
