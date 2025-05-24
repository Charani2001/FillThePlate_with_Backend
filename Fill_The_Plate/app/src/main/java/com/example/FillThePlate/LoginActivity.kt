package com.example.FillThePlate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.FillThePlate.databinding.ActivityLoginBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {

            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val baseUrl = sharedPref.getString("backend_url", "http://10.0.2.2:5000") // default fallback

            val url = "$baseUrl/api/users/login"

            val json = """
        {
           
            "password": "$password",
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
                        Toast.makeText(this@LoginActivity, "Failed to connect: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        if (response.isSuccessful) {
                            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                            sharedPref.edit().putString("username", username).apply()
                       runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                       }
                        } else {
                            val errorBody = JSONObject(response.body?.string() ?:"{}")

                            val errorMessage = errorBody.optString("message","Unknown error")
                            Toast.makeText(this@LoginActivity, "Login Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })


        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
