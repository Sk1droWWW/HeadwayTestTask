package com.example.headwayTestTask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.model.GitHubSearchItemModel

/**
 * Adapter which manages a collection of GitHubSearchItemModel
 * */
class GitHubSearchPagingAdapter(
    private val onClickListener: GitHubSearchViewHolder.OnClickListener
) :
    PagedListAdapter<GitHubSearchItemModel, GitHubSearchViewHolder>
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

class GitHubSearchItemDiffCallback : DiffUtil.ItemCallback<GitHubSearchItemModel>() {

    override fun areItemsTheSame(
        oldItem: GitHubSearchItemModel,
        newItem: GitHubSearchItemModel
    ): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(
        oldItem: GitHubSearchItemModel,
        newItem: GitHubSearchItemModel
    ): Boolean {
        return oldItem == newItem
    }
}
