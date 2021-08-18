package com.example.headwayTestTask.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.headwayTestTask.R
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.network.model.GitHubSearchItemModel
import io.reactivex.subjects.PublishSubject

/**
 * Adapter which manages a collection of GitHubSearchItemModel
 * */
class GitHubSearchAdapter(
    private val mListener: GitHubSearchItemClickListener
) :
    ListAdapter<GitHubSearchItemModel, GitHubSearchAdapter.GitHubSearchViewHolder>
        (GitHubSearchItemDiffCallback())
{

    private var mData : MutableList<GitHubSearchItemModel> =  mutableListOf()

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
        val binding = RepoItemBinding.inflate(inflater, parent, false)

        return GitHubSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GitHubSearchViewHolder, position: Int) {
        holder.bind(mData[position], mListener)
    }

    inner class GitHubSearchViewHolder(
        private val binding: RepoItemBinding
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GitHubSearchItemModel, listener: GitHubSearchItemClickListener) {
            binding.root.setOnClickListener{listener.onGitHubSearchItemClicked(item)}
            binding.repoItemClick = listener

            // TODO refactor
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

    interface GitHubSearchItemClickListener {
        fun onGitHubSearchItemClicked(item: GitHubSearchItemModel)
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
