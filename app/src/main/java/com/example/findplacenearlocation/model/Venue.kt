package com.example.findplacenearlocation.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "venue_db"
)
data class Venue(
    @PrimaryKey
    val id: String,
    val categories: List<Category>,
    val location: Location,
    val name: String,
):Serializable