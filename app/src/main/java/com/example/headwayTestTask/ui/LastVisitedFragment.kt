package com.example.headwayTestTask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headwayTestTask.database.getDatabaseInstance
import com.example.headwayTestTask.databinding.LastVisitedFragmentBinding
import com.example.headwayTestTask.model.GitHubSearchItemModel
import com.example.headwayTestTask.ui.adapter.GithubSearchAdapter
import com.example.headwayTestTask.ui.adapter.GitHubSearchViewHolder
import com.example.headwayTestTask.viewmodels.LastVisitedViewModel
import com.example.headwayTestTask.viewmodels.LastVisitedViewModelFactory


class LastVisitedFragment : Fragment() {

    private lateinit var binding: LastVisitedFragmentBinding
    private lateinit var visitedPagingAdapter: GithubSearchAdapter
    private lateinit var viewModel: LastVisitedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = LastVisitedFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            LastVisitedViewModelFactory()
        )[LastVisitedViewModel::class.java]

        initDatabase()
        initReposRecyclerView()
        observeUi()
    }

    /**
     *  Init database in viewModel
     */
    private fun initDatabase() {
        val dataBaseInstance = getDatabaseInstance(requireContext())
        viewModel.setDatabaseInstance(dataBaseInstance)
    }

    /**
     *  Ser ReposRecyclerView layoutManager and adapter
     */
    private fun initReposRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        visitedPagingAdapter = GithubSearchAdapter(
            object : GitHubSearchViewHolder.OnClickListener {
                override fun onRepoClick(repo: GitHubSearchItemModel) {
                }
            }
        )
        binding.visitedRv.layoutManager = linearLayoutManager
        binding.visitedRv.adapter = visitedPagingAdapter
    }

    /**
     * Set observers for viewModel LiveData fields
     */
    private fun observeUi() {
        viewModel.getReposData()
        viewModel.lastVisitedReposList.observe(requireActivity(), Observer {
            visitedPagingAdapter.submitList(it)
        })
    }
}
