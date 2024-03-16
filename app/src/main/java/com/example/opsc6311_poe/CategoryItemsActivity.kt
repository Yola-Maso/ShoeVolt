package com.example.opsc6311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryItemsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var itemList: MutableList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_items)

        recyclerView = findViewById(R.id.recyclerViewItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemList = mutableListOf()
        itemAdapter = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapter

        val categoryName = intent.getStringExtra("categoryName")
        if (categoryName != null) {
            loadItems(categoryName)
        }
    }

    private fun loadItems(categoryName: String) {
        val database = FirebaseDatabase.getInstance().reference
        val itemsReference = database.child("items")

        itemsReference.orderByChild("category").equalTo(categoryName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    itemList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val item = dataSnapshot.getValue(Item::class.java)
                        item?.let {
                            itemList.add(item)
                        }
                    }
                    itemAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
    }
}
