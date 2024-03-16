package com.example.opsc6311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryListActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryList: MutableList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        recyclerView = findViewById(R.id.recyclerViewCategories)
        recyclerView.layoutManager = LinearLayoutManager(this)
        categoryList = mutableListOf()
        categoryAdapter = CategoryAdapter(categoryList) { category ->
            // Handle category item click event
            val intent = Intent(this, CategoryItemsActivity::class.java)
            intent.putExtra("categoryName", category.categoryName)
            startActivity(intent)
        }
        recyclerView.adapter = categoryAdapter

        loadCategories()
    }

    private fun loadCategories() {
        val database = FirebaseDatabase.getInstance().reference
        val categoriesReference = database.child("categories")

        categoriesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList.clear()
                for (dataSnapshot in snapshot.children) {
                    val category = dataSnapshot.getValue(Category::class.java)
                    category?.let {
                        categoryList.add(category)
                    }
                }
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
