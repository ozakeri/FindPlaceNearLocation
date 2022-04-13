package com.example.findplacenearlocation.db

import androidx.room.TypeConverter
import com.example.findplacenearlocation.model.Icon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class IconConverter {

    @TypeConverter
    fun toString(icon: Icon): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Icon?>?>() {}.type
        return gson.toJson(icon, type)
    }

    @TypeConverter
    fun toIcon(string: String): Icon {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Icon?>?>() {}.type
        return gson.fromJson(string, type)
    }
}