package com.example.flashcards.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Flashcard::class), version = 1, exportSchema = false)
public abstract class FlashcardDatabase : RoomDatabase() {

    abstract fun flashcardDao() : FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: FlashcardDatabase? = null

        fun getDatabase(context: Context): FlashcardDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlashcardDatabase::class.java,
                    "flashcard_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}