package com.example.opsc6311_poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val createCategory: CardView = findViewById(R.id.cardNewCategory)
        createCategory.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@HomeActivity, CreateCategoryActivity::class.java))

        })

        val addItem: CardView = findViewById(R.id.cardAddNewItem)
        addItem.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@HomeActivity, AddItemActivity::class.java))
        })

        val viewCollection: CardView = findViewById(R.id.cardViewCollection)
        viewCollection.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@HomeActivity, CategoryListActivity::class.java))
        })

        val viewProfile: CardView = findViewById(R.id.cardViewProfile)
        viewProfile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@HomeActivity, ProgressActivity::class.java))
        })
    }
}
