package com.example.headwayTestTask.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.headwayTestTask.model.GitHubSearchItemModel


/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

/**
 * DatabaseRepos represents a video entity in the database.
 */
@Entity (tableName = DatabaseRepos.TABLE_NAME)
data class DatabaseRepos constructor(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val name : String,
    val fullName : String,
    val description : String?,
    val htmlUrl : String,
    val updatedAt : String,
    val language : String,
    val stargazersCount : String,
    var visitedFlag : String
    ) {
    companion object {
        const val TABLE_NAME = "Repos"
        const val MAX_SIZE = 20
    }
}

/**
 * Map domain entities to DatabaseVideos
 */
fun GitHubSearchItemModel.asDatabaseEntity() : DatabaseRepos {
    return DatabaseRepos(
            name = this.name,
            fullName = this.fullName,
            description = this.description,
            htmlUrl = this.htmlUrl,
            updatedAt = this.updatedAt,
            language = this.language,
            stargazersCount = this.stargazersCount,
            visitedFlag = this.visitedFlag
        )
}