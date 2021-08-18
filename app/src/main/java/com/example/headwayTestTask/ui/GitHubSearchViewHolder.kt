package com.example.headwayTestTask.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.network.model.GitHubSearchItemModel
import com.example.headwayTestTask.ui.adapter.GitHubSearchAdapter

class GitHubSearchViewHolder(
    private val binding: RepoItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GitHubSearchItemModel, listener: GitHubSearchAdapter.GitHubSearchItemClickListener) {
        binding.root.setOnClickListener { listener.onGitHubSearchItemClicked(item) }
        binding.repoItemClick = listener

        // TODO refactor to @BindingAdapters
        binding.repoName.text = when (item.name.length) {
            in 0..24 -> item.name
            else -> item.name.removeRange(24, item.name.length) + "..."
        }
        binding.repoDescription.text = when (item.description?.length) {
            in 0..24 -> item.description
            else -> item.description?.removeRange(24, item.description.length) + "..."
        }
        binding.repoLastUpdate.text = item.createdAt
        binding.repoLanguage.text = item.language
        binding.repoStargazersCount.text = item.stargazers_count
        binding.repoVisitedFlag.text = item.visitedFlag
    }
}