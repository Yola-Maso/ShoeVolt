package com.example.opsc6311_poe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.Manifest
import java.io.ByteArrayOutputStream


class AddItemActivity : AppCompatActivity() {

    private lateinit var editTextItemCategory: EditText
    private lateinit var editTextDateOfAcquisition: EditText
    private lateinit var editTextItemDescription: EditText
    private lateinit var editTextItemName: EditText
    private lateinit var buttonTakePhoto: Button

    private val PERMISSION_REQUEST_CAMERA = 100
    private val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var database: DatabaseReference

    private var isFirstItemAdded = false
    private var itemCount = 0
    private var achievements: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)


        editTextItemCategory = findViewById(R.id.editTextItemCategory)
        editTextDateOfAcquisition = findViewById(R.id.editTextDateOfAqcuisition)
        editTextItemDescription = findViewById(R.id.editTextItemaDescription)
        editTextItemName = findViewById(R.id.editTextItemName)
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto)

        buttonTakePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                dispatchTakePictureIntent()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            }
        }

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        val buttonAddItem: Button = findViewById(R.id.buttonAddItem)
        buttonAddItem.setOnClickListener {
            val itemCategory = editTextItemCategory.text.toString().trim()
            val dateOfAcquisition = editTextDateOfAcquisition.text.toString().trim()
            val itemDescription = editTextItemDescription.text.toString().trim()
            val itemName = editTextItemName.text.toString().trim()

            if (itemCategory.isEmpty() || itemDescription.isEmpty() || itemName.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter all required fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Check if the category exists in the database
            val categoryReference = database.child("categories").child(itemCategory)
            categoryReference.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val categorySnapshot = task.result
                    if (categorySnapshot.exists()) {
                        // Category exists, save the item to the database
                        saveItemToDatabase(itemCategory, dateOfAcquisition, itemDescription, itemName)
                    } else {
                        // Category does not exist
                        Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error checking category", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Save the image to the database or perform any other necessary action
            saveImageToDatabase(imageBitmap)
        }
    }

    private fun saveItemToDatabase(
        itemCategory: String,
        dateOfAcquisition: String,
        itemDescription: String,
        itemName: String
    ) {
        val itemsReference = database.child("items")

        val item = HashMap<String, Any>()
        item["category"] = itemCategory
        item["dateOfAcquisition"] = dateOfAcquisition
        item["description"] = itemDescription
        item["name"] = itemName

        itemsReference.push().setValue(item)
            .addOnSuccessListener {
                // Item added successfully
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()

                // Update achievement progress
                if (!isFirstItemAdded) {
                    isFirstItemAdded = true
                    achievements.add("Starter")
                    Toast.makeText(this, "Achievement Unlocked: Starter", Toast.LENGTH_SHORT).show()
                }

                itemCount++
                if (itemCount == 3) {
                    achievements.add("Collector")
                    Toast.makeText(this, "Achievement Unlocked: Collector", Toast.LENGTH_SHORT).show()
                } else if (itemCount == 10) {
                    achievements.add("Packrat")
                    Toast.makeText(this, "Achievement Unlocked: Packrat", Toast.LENGTH_SHORT).show()
                }

                // Finish the activity
                finish()
            }
            .addOnFailureListener {
                // Failed to add item
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveImageToDatabase(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        // Save the byte array to the database or perform any other necessary action
    }

}




