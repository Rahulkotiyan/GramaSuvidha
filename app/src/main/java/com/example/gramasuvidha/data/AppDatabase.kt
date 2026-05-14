package com.example.gramasuvidha.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gramasuvidha.models.Feedback
import com.example.gramasuvidha.models.FeedbackDao
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.models.ProjectDao

@Database(entities = [Project::class, Feedback::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun feedbackDao(): FeedbackDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the INSTANCE is not null, return it,
            // if it is, create the database
            return INSTANCE
                    ?: synchronized(this) {
                        val instance =
                                Room.databaseBuilder(
                                                context.applicationContext,
                                                AppDatabase::class.java,
                                                "grama_suvidha_database"
                                        )
                                        .allowMainThreadQueries()
                                        .fallbackToDestructiveMigration()
                                        .build()
                        INSTANCE = instance
                        instance
                    }
        }
    }
}
