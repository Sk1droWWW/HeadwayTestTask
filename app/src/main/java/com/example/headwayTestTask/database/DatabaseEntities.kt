package com.example.headwayTestTask.database

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

/**
 * DatabaseRepos represents a video entity in the database.
 */
@Entity(tableName = DatabaseRepos.TABLE_NAME)
data class DatabaseRepos constructor(
    @PrimaryKey
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val htmlUrl: String,
    val updatedAt: String,
    val language: String,
    val stargazersCount: String,
    val timeWhenAdd: Long,
    var visitedFlag: String
) {
    companion object {
        const val TABLE_NAME = "Repos"
        const val MAX_SIZE = 20
    }
}

