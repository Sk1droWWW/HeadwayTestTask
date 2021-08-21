package com.example.headwayTestTask.repository

import com.example.headwayTestTask.model.GitHubSearchItemModel
import com.example.headwayTestTask.network.service.GithubApiService
import io.reactivex.Observable

class SearchRepository(private val apiService: GithubApiService) {
    fun search(query: String, page: Int): Observable<List<GitHubSearchItemModel>> {
        return apiService.getGitHubRepos(searchParam = query, page = page).flatMap {
            Observable.just(it.items)
        }
    }
}

object SearchRepositoryProvider {
    fun provideSearchRepository(apiService: GithubApiService): SearchRepository {
        return SearchRepository(apiService)
    }
}