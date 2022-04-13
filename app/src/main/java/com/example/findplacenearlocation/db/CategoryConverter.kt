package com.example.findplacenearlocation.db

import androidx.room.TypeConverter
import com.example.findplacenearlocation.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class CategoryConverter {

    @TypeConverter
    fun toString(category: List<Category>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Category?>?>() {}.type
        return gson.toJson(category, type)
    }

    @TypeConverter
    fun toCategory(string: String): List<Category> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Category?>?>() {}.type
        return gson.fromJson<List<Category>>(string, type)
    }
}