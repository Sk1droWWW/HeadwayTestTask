package com.example.headwayTestTask.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
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

    /**
     * Appends a new list of GitHub search items to existing list
     *
     * @param data List<GitHubSearchItemModel>
     * */
    fun addData(data : List<GitHubSearchItemModel>?) {
        if(data?.isNotEmpty() == true) {
            val toAddPos = mData.size
            val addedSize = data.size
            mData.addAll(data)

            notifyItemRangeInserted(toAddPos, addedSize)
//            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = mData.size

    /**
     * Clears both Header and all existing Search items
     *
     * */
    fun clearAll() {
        val size = mData.size
        mData.clear()

        notifyItemRangeRemoved(0, size + 1)
    }

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
            binding.repoName.text = when(item.name.length) {
                in 0..24 -> item.name
                else ->  item.name.removeRange(24, item.name.length) + "..."
            }
            binding.repoDescription.text = when(item.description?.length) {
                in 0..24 -> item.description
                else ->  item.description?.removeRange(24, item.description.length) + "..."
            }
            binding.repoLastUpdate.text = item.createdAt
            binding.repoLanguage.text = item.language
            binding.repoStargazersCount.text = item.stargazers_count
        }
    }

}
