package com.example.headwayTestTask.ui;

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
import com.example.headwayTestTask.network.service.GithubApiService
import com.example.headwayTestTask.repository.SearchRepositoryProvider
import com.example.headwayTestTask.ui.adapter.GitHubSearchAdapter
import com.example.headwayTestTask.viewmodels.MainViewModel
import com.example.headwayTestTask.viewmodels.MainViewModelFactory


class LastVisitedFragment : Fragment() {

    private lateinit var binding: LastVisitedFragmentBinding
    private lateinit var visitedPagingAdapter: GitHubSearchAdapter
    private lateinit var viewModel: MainViewModel

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

        val apiService = GithubApiService.create()
        val repository = SearchRepositoryProvider.provideSearchRepository(apiService)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        initDatabase()
        initReposRecyclerView()
        observeUi()
    }

    private fun initDatabase() {
        val dataBaseInstance = getDatabaseInstance(requireContext())
        viewModel.setDatabaseInstance(dataBaseInstance)
    }

    private fun initReposRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        visitedPagingAdapter = GitHubSearchAdapter(MainFragment.newInstance())
        binding.visitedRv.layoutManager = linearLayoutManager
        binding.visitedRv.adapter = visitedPagingAdapter
    }

    private fun observeUi() {
        viewModel.getPersonData()
        viewModel.lastVisitedReposList.observe(requireActivity(), Observer {
            visitedPagingAdapter.submitList(it.reversed())
        })
    }
}
