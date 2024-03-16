package com.example.opsc6311_poe

data class Item
(
val name: String = "",
val description: String = "",
val category: String = "",
val dateOfAcquisition: String = ""
) {
    // No-argument constructor required by Firebase
    constructor() : this("", "", "", "")
}