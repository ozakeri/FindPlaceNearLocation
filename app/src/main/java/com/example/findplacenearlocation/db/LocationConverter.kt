package com.example.findplacenearlocation.db

import androidx.room.TypeConverter
import com.example.findplacenearlocation.model.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class LocationConverter {

    @TypeConverter
    fun toString(location: Location): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Location?>?>() {}.type
        return gson.toJson(location, type)
    }

    @TypeConverter
    fun toLocation(string: String): Location {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Location?>?>() {}.type
        return gson.fromJson(string, type)
    }
}