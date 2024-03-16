package com.example.opsc6311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ProgressActivity : AppCompatActivity() {

    private lateinit var recyclerViewProgress: RecyclerView
    private lateinit var progressAdapter: ProgressAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        recyclerViewProgress = findViewById(R.id.recyclerViewProgress)
        recyclerViewProgress.layoutManager = LinearLayoutManager(this)
        progressAdapter = ProgressAdapter()
        recyclerViewProgress.adapter = progressAdapter

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        loadProgressData()
    }

    private fun loadProgressData() {
        database.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryList: MutableList<Category> = mutableListOf()
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.key
                    val category = categorySnapshot.getValue(Category::class.java)
                    category?.let {
                        // Calculate progress percentage
                        val progressPercentage = if (it.itemNumber > 0) {
                            (it.numOfItems.toDouble() / it.itemNumber.toDouble()) * 100
                        } else {
                            0.0
                        }
                        it.progressPercentage = progressPercentage.toFloat()
                        categoryList.add(it)
                    }
                }

                progressAdapter.setCategories(categoryList)
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}