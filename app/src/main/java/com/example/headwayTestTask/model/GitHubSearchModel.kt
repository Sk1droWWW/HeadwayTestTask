package com.example.headwayTestTask.model

import com.google.gson.annotations.SerializedName

data class GitHubSearchModel(
        @SerializedName("incomplete_results")   val incompleteResults: Boolean,
        @SerializedName("total_count")          val totalCount: Int,
        @SerializedName("items")                val items: List<GitHubSearchItemModel>
)