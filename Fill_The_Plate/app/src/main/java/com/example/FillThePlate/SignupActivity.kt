package com.example.FillThePlate

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle signup button click
        binding.signupButton.setOnClickListener {
            val name = binding.signupname.text.toString()
            val email = binding.signupemail.text.toString()
            val username = binding.signupUsername.text.toString()
            val password = binding.signupPassword.text.toString()

            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val baseUrl = sharedPref.getString("backend_url", "http://10.0.2.2:5000") // default fallback

            val url = "$baseUrl/api/users/signup"

            val json = """
        {
            "email": "$email",
            "password": "$password",
            "fullName": "$name",
            "userName": "$username"
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
                    runOnUiThread {
                        Toast.makeText(this@SignupActivity, "Failed to connect: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this@SignupActivity)
                        sharedPref.edit().putString("username", username).apply()

                        runOnUiThread {
                            Toast.makeText(this@SignupActivity, "Signup successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@SignupActivity, "Signup failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            })
        }

        // Handle redirect to LoginActivity
        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}
