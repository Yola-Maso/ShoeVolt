package com.example.opsc6311_poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateCategoryActivity : AppCompatActivity() {

    private lateinit var edCategoryName: EditText
    private lateinit var edNumOfItems: EditText
    private lateinit var btn: Button
    private lateinit var btnCancel: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        edCategoryName = findViewById(R.id.editTextCategoryName)
        edNumOfItems = findViewById(R.id.editTextNumOfItems)
        btn = findViewById(R.id.buttonAddCategory)
        btnCancel = findViewById(R.id.buttonCancel)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        btnCancel.setOnClickListener {
            startActivity(Intent(this@CreateCategoryActivity, HomeActivity::class.java))
        }

        btn.setOnClickListener {
            val categoryName = edCategoryName.text.toString()
            val itemNumber = edNumOfItems.text.toString()

            if (categoryName.isEmpty() || itemNumber.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill in all the details", Toast.LENGTH_SHORT).show()
            } else {
                if (itemNumber.matches("\\d+".toRegex())) {
                    // The user entered a number
                    val number = itemNumber.toInt()
                    createNewCategory(categoryName, number)
                } else {
                    // The user did not enter a number
                    Toast.makeText(applicationContext, "Please enter a numerical value for number of items", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createNewCategory(categoryName: String, itemNumber: Int) {
        val category = Category(categoryName, itemNumber)
        database.child("categories").child(categoryName).setValue(category)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Category Created", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@CreateCategoryActivity, HomeActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to create category", Toast.LENGTH_SHORT).show()
            }
    }
}

//data class Category(val categoryName: String, val itemNumber: Int)


