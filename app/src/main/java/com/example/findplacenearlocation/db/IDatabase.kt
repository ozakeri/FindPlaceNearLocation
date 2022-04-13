package com.example.findplacenearlocation.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.findplacenearlocation.model.Venue

@Dao
interface IDatabase {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(venue: Venue)

    @Query("SELECT * FROM venue_db")
    fun getAll():LiveData<List<Venue>>

    @Delete
    suspend fun deleteFromDb(venue: Venue)
}