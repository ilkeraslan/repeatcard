package com.example.flashcards.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

public abstract class FlashcardRoomDatabase : RoomDatabase() {

    abstract fun flashcardDao() : FlashcardDao

    companion object {
        // Singleton
        @Volatile
        private var INSTANCE: FlashcardRoomDatabase? = null

        fun getDatabase(context: Context): FlashcardRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlashcardRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}