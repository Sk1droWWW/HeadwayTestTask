package com.example.headwayTestTask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.model.GitHubSearchItemModel

private const val MAX_CHARACTER_NUMBER = 24

class GitHubSearchViewHolder(
    private val binding: RepoItemBinding,
    private val onClickListener: OnClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GitHubSearchItemModel) {
        binding.root.setOnClickListener { onClickListener.onRepoClick(item) }

        binding.repoName.text = when (item.name.length) {
            in 0..MAX_CHARACTER_NUMBER -> item.name
            else -> item.name.removeRange(MAX_CHARACTER_NUMBER, item.name.length) + "..."
        }
        binding.repoDescription.text = when (item.description?.length) {
            in 0..MAX_CHARACTER_NUMBER -> item.description
            else -> (item.description?.removeRange(MAX_CHARACTER_NUMBER, item.description.length)
                ?: "") + "..."
        }
        binding.repoLastUpdate.text = item.updatedAt
        binding.repoLanguage.text = item.language
        binding.repoStargazersCount.text = item.stargazersCount
        binding.repoVisitedFlag.text = item.visitedFlag
    }

    interface OnClickListener {
        fun onRepoClick(repo: GitHubSearchItemModel)
    }
}