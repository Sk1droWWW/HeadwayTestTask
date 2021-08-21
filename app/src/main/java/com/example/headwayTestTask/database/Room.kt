package com.example.headwayTestTask.database

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase


@Database(entities = [DatabaseRepos::class], version = 1)
abstract class ReposDatabase: RoomDatabase() {
    abstract val repoDao: RepoDao
}

private lateinit var INSTANCE: ReposDatabase

fun getDatabaseInstance(context: Context): ReposDatabase {
    synchronized(ReposDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                ReposDatabase::class.java,
                "repos").build()
        }
    }
    return INSTANCE
}