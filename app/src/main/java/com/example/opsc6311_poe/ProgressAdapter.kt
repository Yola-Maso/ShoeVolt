package com.example.opsc6311_poe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProgressAdapter : RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    private val categoryList: MutableList<Category> = mutableListOf()

    fun setCategories(categories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
        return ProgressViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val textViewCategoryName: TextView = itemView.findViewById(R.id.textViewCategoryName)
        private val textViewProgress: TextView = itemView.findViewById(R.id.textViewProgress)

        fun bind(category: Category) {
            val categoryName = category.categoryName
            val numOfItems = category.numOfItems // Use numOfItems property
            val goalNumber = category.itemNumber // Use itemNumber property

            textViewCategoryName.text = categoryName
            progressBar.max = goalNumber
            progressBar.progress = numOfItems
            textViewProgress.text = "$numOfItems / $goalNumber"
        }
    }
}