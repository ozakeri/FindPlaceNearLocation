package com.example.findplacenearlocation.repository

import com.example.findplacenearlocation.api.RetrofitInstance
import com.example.findplacenearlocation.db.VenueDatabase
import com.example.findplacenearlocation.model.Venue

class VenueRepository(val database: VenueDatabase) {

    suspend fun getNearPlaces(lng: String,limit : Int) = RetrofitInstance.api.getNearPlaces(lng,limit)

    suspend fun getDetailPlace(venueId: String) = RetrofitInstance.api.getDetailPlace(venueId)

    suspend fun upsert(venue: Venue) = database.getIDatabase().upsert(venue)

    suspend fun delete(venue: Venue) = database.getIDatabase().deleteFromDb(venue)

    fun getSaveVenus() = database.getIDatabase().getAll()
}