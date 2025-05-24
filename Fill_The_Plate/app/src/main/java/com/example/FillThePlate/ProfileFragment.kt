package com.example.FillThePlate


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.FillThePlate.databinding.ActivitySignupBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class ProfileFragment : Fragment() {


    private lateinit var nameText: TextView
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var passwordText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        nameText = view.findViewById(R.id.txt_name)
        usernameText = view.findViewById(R.id.txt_username)
        emailText = view.findViewById(R.id.txt_website)
        passwordText = view.findViewById(R.id.txt_bio)


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val baseUrl = sharedPref.getString("backend_url", "http://10.0.2.2:5000") // default fallback

        val userName = sharedPref.getString("username", null)


        fetchUserData();

        val refreshButton = view.findViewById<Button>(R.id.btn_refresh)
        refreshButton.setOnClickListener {
            fetchUserData()
        }



        // Find the logout button
        val logoutButton: Button = view.findViewById(R.id.btn_logout)

        // Set up the click listener for the logout button
        logoutButton.setOnClickListener {
            // Start the LoginActivity when the user clicks the logout button
            val intent = Intent(requireActivity(), LoginActivity::class.java)

            // Optional: Clear the activity stack to prevent returning to the profile screen
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Start the login activity
            startActivity(intent)

            // Optionally, you can finish the current activity (if in Activity context)
            requireActivity().finish()
        }

        val deleteAccountButton :Button =view.findViewById(R.id.btn_delete)
        // Set up the click listener for the logout button
        deleteAccountButton.setOnClickListener {

            val client = OkHttpClient()

            val deleteurl = "$baseUrl/api/users/deleteuser?userName=${userName}"

            val request = Request.Builder().url(deleteurl).delete().build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {

                    if (response.isSuccessful) {
                    val body = response.body?.string()

                        val messageBody = JSONObject(body)

                        val message = messageBody.optString("message","Unknown error")

                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Delete Response: $message", Toast.LENGTH_SHORT).show()
                    }
                    // Start the LoginActivity when the user clicks the logout button
                    val intent = Intent(requireActivity(), LoginActivity::class.java)// Optional: Clear the activity stack to prevent returning to the profile screen
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        // Start the login activity
                        startActivity(intent)

                        // Optionally, you can finish the current activity (if in Activity context)
                        requireActivity().finish()
                }else {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            })




        }


        val updateProfileButton :Button =view.findViewById(R.id.btn_update)
        // Set up the click listener for the logout button
        updateProfileButton.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.dialog_update_profile, null)
            val dialog = AlertDialog.Builder(requireActivity())
                .setView(dialogView)
                .setTitle("Update Profile")
                .setCancelable(true)
                .create()

            val fullNameInput = dialogView.findViewById<EditText>(R.id.et_full_name)

            val emailInput = dialogView.findViewById<EditText>(R.id.et_email)
            val passwordInput = dialogView.findViewById<EditText>(R.id.et_password)
            val submitBtn = dialogView.findViewById<Button>(R.id.btn_submit_update)

            submitBtn.setOnClickListener {


                val fullname = fullNameInput.text.toString()
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                val updateurl = "$baseUrl/api/users/updateuser"

                val json = """
        {
            "email": "$email",
            "password": "$password",
            "fullName": "$fullname",
            "userName": "$userName"
        }
    """.trimIndent()

                val client = OkHttpClient()
                val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)
                val request = Request.Builder()
                    .url(updateurl)
                    .put(requestBody)
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
                                Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_SHORT).show()

                            }
                            fetchUserData()
                            dialog.dismiss()
                        } else {
                            requireActivity(). runOnUiThread {
                                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                            }
                            fetchUserData()
                            dialog.dismiss()
                        }
                    }

                })


            }

            dialog.show()


        }


        return view
    }

    fun fetchUserData(){

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val baseUrl = sharedPref.getString("backend_url", "http://10.0.2.2:5000")
        val userName = sharedPref.getString("username", null)

        val geturl = "$baseUrl/api/users/readsingle?userName=${userName}"


        if (userName != null) {


            val client = OkHttpClient()

            val request = Request.Builder()
                .url(geturl)
                .get()
                .build()



            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val jsonObject = JSONObject(responseBody!!)
                        val userData = jsonObject.getJSONArray("data").getJSONObject(0)

                        val fullName = userData.getString("full_name")
                        val userName = userData.getString("user_name")
                        val email = userData.getString("email")
                        val password = userData.getString("password")

                        requireActivity().runOnUiThread {
                            nameText.text = fullName
                            usernameText.text = userName
                            emailText.text = email
                            passwordText.text = password
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

        } else {
            Toast.makeText(requireContext(), "Username not found", Toast.LENGTH_SHORT).show()
        }
    }
}
