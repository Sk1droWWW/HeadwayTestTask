package com.example.headwayTestTask.network.repository

import com.example.headwayTestTask.network.GitHubSearchApi

/**
 * GitHubSearchRepository for fetching GitHubSearchModel
 * This class coordinates between various data sources (API, Local Db, Memory)
 */
class GitHubSearchRepositoryImpl(
    private val mApiSource : GitHubSearchApi
    ) : GitHubSearchRepository {

    override fun searchGitHubRepo(searchParam: String,
                                  page: Int,
                                  sort: String,
                                  order: String,
                                  perPage: Int) =
            mApiSource.searchGitHubRepo(searchParam, page, sort, order, perPage)
}