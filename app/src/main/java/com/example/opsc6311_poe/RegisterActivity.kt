package com.example.opsc6311_poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var edPassword: EditText
    private lateinit var edEmail: EditText
    private lateinit var edUsername: EditText
    private lateinit var edConfirm: EditText
    private lateinit var btn: Button
    private lateinit var tv: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edPassword = findViewById(R.id.editTextRegPassword)
        edUsername = findViewById(R.id.editTextRegUsername)
        edEmail = findViewById(R.id.editTextRegEmail)
        edConfirm = findViewById(R.id.editTextRegConfirmPassword)
        btn = findViewById(R.id.buttonRegister)
        tv = findViewById(R.id.textViewExistingUser)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        tv.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        btn.setOnClickListener {
            val username = edUsername.text.toString()
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            val confirm = edConfirm.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill in all the details", Toast.LENGTH_SHORT).show()
            } else {
                if (password == confirm) {
                    if (isValid(password)) {
                        registerUser(username, email, password)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "The Password must contain at least 8 characters containing a letter, digit, and a special symbol",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Password and Confirm Password don't match", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        val user = User(username, email, password)
        database.child("users").child(username).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "User Registered", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to register user", Toast.LENGTH_SHORT).show()
            }
    }

    private fun isValid(passwordhere: String): Boolean {
        var f1 = false
        var f2 = false
        var f3 = false

        if (passwordhere.length >= 8) {
            for (p in passwordhere) {
                if (p.isLetter()) {
                    f1 = true
                }
            }
            for (r in passwordhere) {
                if (r.isDigit()) {
                    f2 = true
                }
            }
            for (s in passwordhere) {
                val c = s
                if (c.toInt() in 33..46 || c == 64.toChar()) {
                    f3 = true
                }
            }
        }

        return f1 && f2 && f3
    }

}

data class User(val username: String, val email: String, val password: String)


