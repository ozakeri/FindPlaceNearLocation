package com.example.findplacenearlocation.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.findplacenearlocation.model.Venue

@TypeConverters(LocationConverter::class, CategoryConverter::class)
@Database(entities = [Venue::class], version = 1)
abstract class VenueDatabase : RoomDatabase() {

    abstract fun getIDatabase(): IDatabase

    companion object {
        @Volatile
        private var instance: VenueDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                VenueDatabase::class.java,
                "venue_db.db"
            ).build()
    }
}