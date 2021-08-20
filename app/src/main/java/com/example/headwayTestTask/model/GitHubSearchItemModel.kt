package com.example.headwayTestTask.model

import com.google.gson.annotations.SerializedName


data class GitHubSearchItemModel (
        @SerializedName("id")               val id : Long,
        @SerializedName("name")             val name : String,
        @SerializedName("full_name")        val fullName : String,
        @SerializedName("owner")            val owner : Owner,
        @SerializedName("description")      val description : String?,
        @SerializedName("html_url")         val htmlUrl : String,
        @SerializedName("created_at")       val createdAt : String,
        @SerializedName("updated_at")       val updatedAt : String,
        @SerializedName("language")         val language : String,
        @SerializedName("stargazers_count") val stargazers_count : String,
        var visitedFlag : String = "")
{
    data class Owner (
            @SerializedName("id")               val id : Int,
            @SerializedName("login")            val loginName : String,
            @SerializedName("avatar_url")       val thumbnailUrl : String,
            @SerializedName("html_url")         val htmlUrl : String
    )

}