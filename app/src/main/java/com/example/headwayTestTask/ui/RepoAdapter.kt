package com.example.headwayTestTask.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.headwayTestTask.R
import com.example.headwayTestTask.databinding.RepoItemBinding

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.

class RepoAdapter: RecyclerView.Adapter<RepoViewHolder>() {

    /**
     * The videos that our Adapter will show
     */
    var repos: List<RepoItem> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val withDataBinding: RepoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            RepoViewHolder.LAYOUT,
            parent,
            false)
        return RepoViewHolder(withDataBinding)
    }

    override fun getItemCount() = repos.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.repo = repos[position]
        }
    }

   /* fun updateList(list: List<Owner>) {
        repos = list as MutableList<Owner>
        notifyDataSetChanged()
    }*/

}

/**
 * Click listener for Repos. By giving the block a name it helps a reader understand what it does.
 *
 */


/**
 * ViewHolder for DevByte items. All work is done by data binding.
// */
//class RepoViewHolder(val viewDataBinding: RepoItemBinding) :
//    RecyclerView.ViewHolder(viewDataBinding.root), View.OnClickListener {
//
//    init {
//        itemView.setOnClickListener(this)
//    }
//
//    companion object {
//        @LayoutRes
//        val LAYOUT = R.layout.repo_item
//    }
//
//   /* fun bindData(owner: Owner) {
//        itemView.textViewName.text = owner.login
//
//    }
//
//    override fun onClick(view: View?) {
//        onItemClickListener.onItemClick(items[adapterPosition])
//    }
//
//    interface OnItemClickListener {
//        fun onItemClick(owner: Owner)
//    }*/
//}
 */
