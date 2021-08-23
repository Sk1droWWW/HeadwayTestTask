package com.example.headwayTestTask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DatabaseRepos::class], version = 1)
abstract class ReposDatabase : RoomDatabase() {
    abstract val repoDao: RepoDao
}

private lateinit var INSTANCE: ReposDatabase

/**
 * Template function to retrieve a database instance
 *
 * @param context activity context
 * @return ReposDatabase instance
 */
fun getDatabaseInstance(context: Context): ReposDatabase {
    synchronized(ReposDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ReposDatabase::class.java,
                "repos"
            ).build()
        }
    }
    return INSTANCE
}