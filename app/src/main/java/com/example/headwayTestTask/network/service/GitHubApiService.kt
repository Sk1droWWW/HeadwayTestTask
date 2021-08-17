package com.example.headwayTestTask.network.service

import com.example.headwayTestTask.network.model.GitHubSearchModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/repositories")
    fun searchGitHubRepo(
        @Query("q")         searchParam : String,
        @Query("sort")      sort : String = "stars",
        @Query("order")     order : String = "desc",
        @Query("per_page")  perPage : Int = 15) : Observable<GitHubSearchModel>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): GithubApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()

            return retrofit.create(GithubApiService::class.java);
        }
    }
}

class SearchRepository(private val apiService: GithubApiService) {
    fun searchGitHubRepo(repoName: String): Observable<GitHubSearchModel> {
        return apiService.searchGitHubRepo(searchParam = repoName)
    }
}

object SearchRepositoryProvider {
    fun provideSearchRepository(apiService: GithubApiService): SearchRepository {
        return SearchRepository(apiService)
    }
}
