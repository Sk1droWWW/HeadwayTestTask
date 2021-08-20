package com.example.headwayTestTask.network.service

import com.example.headwayTestTask.model.GitHubSearchItemModel
import com.example.headwayTestTask.model.GitHubSearchModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/repositories")
    fun getGitHubRepos(
        @Query("q")         searchParam : String,
        @Query("since")     userId: Int,
        @Query("sort")      sort : String = "stars",
        @Query("order")     order : String = "desc",
        @Query("per_page")  perPage : Int = 15,
        @Query("page")      page: Int = 1
    ) : Observable<GitHubSearchModel>

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

            return retrofit.create(GithubApiService::class.java)
        }
    }
}

class SearchRepository(private val apiService: GithubApiService) {
    fun search(query: String, page: Int): Observable<List<GitHubSearchItemModel>> {
        return apiService.getGitHubRepos(query, page).flatMap {
            Observable.just(it.items)
        }
    }
}

object SearchRepositoryProvider {
    fun provideSearchRepository(apiService: GithubApiService): SearchRepository {
        return SearchRepository(apiService)
    }
}
