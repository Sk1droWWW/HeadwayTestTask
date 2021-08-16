package com.example.headwayTestTask.network.datasource

import com.example.headwayTestTask.network.model.GitHubSearchModel
import io.reactivex.Observable

/**
 * Interface that represents a Data Source for getting {@link GitHubSearchModel} related data.
 * Every data source (API, Memory, Local Database) that needs to fetch GitHubSearchModel must implement
 * this interface.
 *
 */
interface GitHubSearchDataSource {

    fun searchGitHubRepo(
            searchParam : String,
            page : Int,
            sort : String = "",
            order : String = "desc",
            perPage : Int = 15) : Observable<GitHubSearchModel>
}