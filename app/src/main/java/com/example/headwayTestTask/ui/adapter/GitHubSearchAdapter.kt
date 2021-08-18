package com.example.headwayTestTask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.headwayTestTask.databinding.RepoItemBinding
import com.example.headwayTestTask.network.model.GitHubSearchItemModel
import com.example.headwayTestTask.ui.GitHubSearchViewHolder

/**
 * Adapter which manages a collection of GitHubSearchItemModel
 * */
class GitHubSearchAdapter(
    private var mData: MutableList<GitHubSearchItemModel>,
    private val mListener: GitHubSearchItemClickListener
) :
    ListAdapter<GitHubSearchItemModel, GitHubSearchViewHolder>
        (GitHubSearchItemDiffCallback()) {

    /**
     * Appends a new list of GitHub search items to existing list
     *
     * @param data List<GitHubSearchItemModel>
     * */
    fun addData(data: List<GitHubSearchItemModel>?) {
        if (data?.isNotEmpty() == true) {
            val toAddPos = mData.size
            val addedSize = data.size
            mData.addAll(data)

            notifyItemRangeInserted(toAddPos, addedSize)
        }
    }

    /**
     * Clears both Header and all existing Search items
     *
     * */
    fun clearAll() {
        val size = mData.size
        mData.clear()

        notifyItemRangeRemoved(0, size)
    }

    override fun getItemCount() = mData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoItemBinding.inflate(inflater, parent, false)

        return GitHubSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GitHubSearchViewHolder, position: Int) {
        holder.bind(mData[holder.adapterPosition], mListener)
    }

    interface GitHubSearchItemClickListener {
        fun onGitHubSearchItemClicked(item: GitHubSearchItemModel?)
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
