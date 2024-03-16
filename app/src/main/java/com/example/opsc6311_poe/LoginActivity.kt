package com.example.opsc6311_poe

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var edPassword: EditText
    private lateinit var edUsername: EditText
    private lateinit var btn: Button
    private lateinit var tv: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edPassword = findViewById(R.id.editTextTextLoginPassword)
        edUsername = findViewById(R.id.editTextTextLoginUsername)
        btn = findViewById(R.id.buttonLogin)
        tv = findViewById(R.id.textViewNewUser)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        btn.setOnClickListener {
            val username = edUsername.text.toString()
            val password = edPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill in all the details", Toast.LENGTH_SHORT).show()
            } else {
                login(username, password)
            }
        }

        tv.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun login(username: String, password: String) {
        database.child("users").child(username).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    val storedPassword = snapshot.child("password").value as String
                    if (password == storedPassword) {
                        Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()
                        saveUsername(username)
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    } else {
                        Toast.makeText(applicationContext, "Invalid Username and/or Password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Invalid Username and/or Password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Failed to login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUsername(username: String) {
        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }

    }
