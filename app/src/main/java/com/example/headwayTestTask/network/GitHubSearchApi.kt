package com.example.headwayTestTask.network

import com.example.headwayTestTask.network.datasource.GitHubSearchDataSource
import com.example.headwayTestTask.network.model.GitHubSearchModel
import com.example.headwayTestTask.network.service.GitHubSearchService
import io.reactivex.Observable
import retrofit2.Retrofit

/**
 * API data source for fetching GitHubSearchModel
 */
class GitHubSearchApi(private val mRetrofitBuilder : Retrofit.Builder) : GitHubSearchDataSource {

    override fun searchGitHubRepo(searchParam: String,
                                  page: Int,
                                  sort: String,
                                  order: String,
                                  perPage: Int) : Observable<GitHubSearchModel> {
        val retrofit = mRetrofitBuilder
                .baseUrl("https://api.github.com")
                .build()
        val gitHubSearchService = retrofit.create(GitHubSearchService::class.java)
        return gitHubSearchService.searchGitHubRepo(searchParam, page, sort, order, perPage)
    }

}