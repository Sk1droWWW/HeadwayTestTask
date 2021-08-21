package com.example.headwayTestTask.model

import com.google.gson.annotations.SerializedName


data class GitHubSearchItemModel (
        @SerializedName("id")               val id : Long,
        @SerializedName("name")             val name : String,
        @SerializedName("full_name")        val fullName : String,
        @SerializedName("description")      val description : String?,
        @SerializedName("html_url")         val htmlUrl : String,
        @SerializedName("updated_at")       val updatedAt : String,
        @SerializedName("language")         val language : String,
        @SerializedName("stargazers_count") val stargazersCount : String,
        var visitedFlag : String = "")
