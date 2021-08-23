package com.example.headwayTestTask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.model.GitHubSearchItemModel

class GithubSearchAdapter(
    private val onClickListener: GitHubSearchViewHolder.OnClickListener
) :
    ListAdapter<GitHubSearchItemModel, GitHubSearchViewHolder>
        (GitHubSearchItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoItemBinding.inflate(inflater, parent, false)

        return GitHubSearchViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: GitHubSearchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}