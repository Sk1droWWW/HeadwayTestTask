package com.example.headwayTestTask.network.repository

import com.example.headwayTestTask.network.model.GitHubSearchModel
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting {@link GitHubSearchModel} related data.
 */
interface GitHubSearchRepository {

    fun searchGitHubRepo(
        searchParam : String,
        page : Int,
        sort : String = "",
        order : String = "desc",
        perPage : Int = 15 ) : Observable<GitHubSearchModel>
}