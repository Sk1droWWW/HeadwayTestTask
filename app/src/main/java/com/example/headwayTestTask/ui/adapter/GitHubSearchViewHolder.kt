package com.example.headwayTestTask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.model.GitHubSearchItemModel

class GitHubSearchViewHolder(
    private val binding: RepoItemBinding,
    private val onClickListener: OnClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GitHubSearchItemModel) {
        binding.root.setOnClickListener { onClickListener.onRepoClick(item) }
//        binding.repoItemClick = listener

        // TODO refactor to @BindingAdapters
        binding.repoName.text = when (item.name.length) {
            in 0..24 -> item.name
            else -> item.name.removeRange(24, item.name.length) + "..."
        }
        binding.repoDescription.text = when (item.description?.length) {
            in 0..24 -> item.description
            else -> item.description?.removeRange(24, item.description.length) + "..."
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