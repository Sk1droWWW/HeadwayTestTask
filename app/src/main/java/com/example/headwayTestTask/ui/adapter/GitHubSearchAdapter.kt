package com.example.headwayTestTask.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headwayTestTask.R
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.network.model.GitHubSearchItemModel

/**
 * Adapter which manages a collection of GitHubSearchItemModel
 * */
class GitHubSearchAdapter :
    RecyclerView.Adapter<GitHubSearchAdapter.GitHubSearchViewHolder>()
{

    private var mData : MutableList<GitHubSearchItemModel> = mutableListOf()

    override fun getItemCount() = mData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoItemBinding.inflate(inflater)

        return GitHubSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GitHubSearchViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class GitHubSearchViewHolder(
        private val binding: RepoItemBinding
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GitHubSearchItemModel) {
            binding.repoName.text = item.name
            binding.repoDescription.text = item.description
            binding.repoLastUpdate.text = item.createdAt
            binding.repoStars.text = item.stars.toString()
        }
    }

}

/*
class GitHubSearchListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(item: GitHubSearchItemModel) = clickListener(item.id.toLong())
}*/
