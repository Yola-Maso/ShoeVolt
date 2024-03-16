package com.example.opsc6311_poe

data class Category(

    val categoryName: String = "",
    val itemNumber: Int = 0,
    var numOfItems: Int = 0, // Add the numOfItems property
    var progressPercentage: Float = 0.0f // Add the progressPercentage property
) {
    // No-argument constructor required by Firebase
    constructor() : this("", 0, 0, 0.0f)
}