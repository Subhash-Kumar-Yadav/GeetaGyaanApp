package com.example.geetagyaan.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SavedChapters::class , VersesSave::class] , version = 1 , exportSchema = false)
@TypeConverters(AppTypeConverter::class)
abstract class AppRoomDB: RoomDatabase() {

    abstract fun savedChaptersDao() : SavedChaptersDao
    abstract fun savedVerseDao() : SavedVersesDao

    companion object{
        @Volatile
        var instance : AppRoomDB ?= null

        fun getDatabaseInstance(context: Context): AppRoomDB? {
            val tempInstance = instance
            if(instance != null) return tempInstance
            synchronized(this){
                val roomDb = Room.databaseBuilder(context , AppRoomDB::class.java , "AppDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
                instance = roomDb
                return roomDb
            }
        }
    }
}