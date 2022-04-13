package com.example.findplacenearlocation.model

data class Location(
    val address: String,
    val distance: Int,
    val lat: Double,
    val lng: Double,
)