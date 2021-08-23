package com.example.headwayTestTask.network.service

import com.example.headwayTestTask.model.GitHubSearchModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubApiService {
    /**
     * Search repositories query
     *
     * @param accessToken Oauth authorization token
     * @param searchParam repository which we want to search
     * @param sort sort option
     * @param order sort order
     * @param perPage number of repos per request
     * @param page downloaded page number
     * @return
     */
    @GET("search/repositories")
    fun getGitHubRepos(
        @Header("Authorization") accessToken: String,
        @Query("q")         searchParam : String,
        @Query("sort")      sort : String = "stars",
        @Query("order")     order : String = "desc",
        @Query("per_page")  perPage : Int = 15,
        @Query("page")      page: Int = 1
    ): Observable<GitHubSearchModel>

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


