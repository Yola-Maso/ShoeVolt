package com.example.opsc6311_poe

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var btngetStarted: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btngetStarted = findViewById(R.id.btnGetStarted)

        btngetStarted.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))

        }

    }
}