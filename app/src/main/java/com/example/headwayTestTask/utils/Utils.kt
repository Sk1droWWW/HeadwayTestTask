package com.example.headwayTestTask.utils

import com.example.headwayTestTask.database.DatabaseRepos
import com.example.headwayTestTask.model.GitHubSearchItemModel

/**
 * Map DatabaseVideos to domain entities
 */
fun List<DatabaseRepos>.asDomainModel(): List<GitHubSearchItemModel> {
    return map {
        GitHubSearchItemModel(
            id = it.id,
            name = it.name,
            fullName = it.fullName,
            description = it.description,
            htmlUrl = it.htmlUrl,
            updatedAt = it.updatedAt,
            language = it.language,
            stargazersCount = it.stargazersCount,
            visitedFlag = it.visitedFlag
        )
    }
}

/**
 * Map domain entity to DatabaseVideo
 */
fun GitHubSearchItemModel.asDatabaseEntity(): DatabaseRepos {
    return DatabaseRepos(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        description = this.description,
        htmlUrl = this.htmlUrl,
        updatedAt = this.updatedAt,
        language = this.language,
        stargazersCount = this.stargazersCount,
        timeWhenAdd = System.currentTimeMillis(),
        visitedFlag = this.visitedFlag
    )
}

enum class AuthenticationState {
    AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
}