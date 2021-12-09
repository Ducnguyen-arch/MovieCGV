package com.ducnn17.movieCGV.data.movies.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ducnn17.movieCGV.data.movies.entity.Converters
import com.ducnn17.movieCGV.data.movies.entity.Result

@Database(entities = [Result::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao
    companion object {
        private const val DB_NAME = "AppDatabase"
        @Volatile
        private var INSTANCE : AppDatabase? = null
        fun getDataBase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
                    DB_NAME).allowMainThreadQueries()
                    .build()
                INSTANCE = instance

                //return
                instance
            }
        }
    }
}
