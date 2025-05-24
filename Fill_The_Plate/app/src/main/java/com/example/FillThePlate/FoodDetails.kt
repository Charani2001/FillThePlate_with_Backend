package com.example.myapplication

import DonationAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


import android.util.Log
import androidx.preference.PreferenceManager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FillThePlate.Donation
import okhttp3.*
import org.json.JSONArray
import com.example.FillThePlate.R
import java.io.IOException

class FoodDetailsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.food_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.donationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchDonations()
    }

    private fun fetchDonations() {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val baseUrl = sharedPref.getString("backend_url", "http://10.0.2.2:5000") // default fallback

        val url = "$baseUrl/api/donations/get"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FoodDetails", "Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val jsonArray = JSONArray(org.json.JSONObject(jsonString).getString("data"))
                    val donations = mutableListOf<Donation>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        donations.add(
                            Donation(
                                foodName = item.getString("food_name"),
                                servings = item.getInt("servings"),
                                location = item.getString("pickup_location"),
                                donator =item.getString("full_name"),
                                contact =item.getString("contact_number"),
                                type =item.getString("food_type")


                            )
                        )
                    }

                    activity?.runOnUiThread {
                        recyclerView.adapter = DonationAdapter(donations) { selectedDonation ->
                            // Handle item click here
                            loadGetFoodFragment()
                        }
                    }
                }
            }
        })
    }

    private fun loadGetFoodFragment() {
        val fragment = GetFoodFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}