package com.example.headwayTestTask.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headwayTestTask.R
import com.example.headwayTestTask.database.getDatabaseInstance
import com.example.headwayTestTask.databinding.MainFragmentBinding
import com.example.headwayTestTask.network.NetworkStatus
import com.example.headwayTestTask.model.GitHubSearchItemModel
import com.example.headwayTestTask.network.service.GithubApiService
import com.example.headwayTestTask.repository.SearchRepositoryProvider
import com.example.headwayTestTask.ui.adapter.GitHubSearchPagingAdapter
import com.example.headwayTestTask.ui.adapter.GitHubSearchViewHolder
import com.example.headwayTestTask.utils.AuthenticationState
import com.example.headwayTestTask.utils.asDatabaseEntity
import com.example.headwayTestTask.viewmodels.MainViewModel
import com.example.headwayTestTask.viewmodels.MainViewModelFactory
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import io.reactivex.disposables.CompositeDisposable


class MainFragment : Fragment(), GitHubSearchViewHolder.OnClickListener {

    companion object {
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001

        fun newInstance() = MainFragment()
    }

//    private val mainFragmentViewModel by viewModels<MainViewModel>()
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var mainFragmentViewModel: MainViewModel

    private lateinit var binding: MainFragmentBinding
    private lateinit var searchPagingAdapter: GitHubSearchPagingAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = GithubApiService.create()
        val repository = SearchRepositoryProvider.provideSearchRepository(apiService)
//        mainFragmentViewModel = MainViewModel(repository)
        mainFragmentViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        initDatabase()
        initReposRecyclerView()
        observeAuthenticationState()
        observeUi()

        binding.loginBtn.setOnClickListener { launchSignInFlow() }
        binding.searchBtn.setOnClickListener { searchGitHubRepos() }
        binding.navToLastVisitedBtn.setOnClickListener { openLastVisitedFragment() }
    }

    private fun initDatabase() {
        val dataBaseInstance = getDatabaseInstance(requireContext())
        mainFragmentViewModel.setDatabaseInstance(dataBaseInstance)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Log.i(
                    TAG,
                    "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun initReposRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        searchPagingAdapter = GitHubSearchPagingAdapter(this)
        binding.searchResultRv.layoutManager = linearLayoutManager
        binding.searchResultRv.adapter = searchPagingAdapter
    }

    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {

        mainFragmentViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    binding.welcomeTv.text = getPersonalizationMessage()

                    binding.loginBtn.text = getString(R.string.logout_button_text)
                    binding.loginBtn.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }
                }
                else -> {
                    binding.welcomeTv.text = resources.getString(R.string.welcome_message)

                    binding.loginBtn.text = getString(R.string.login_btn_text)
                    binding.loginBtn.setOnClickListener {
                        launchSignInFlow()
                    }
                }
            }
        })
    }

    private fun observeUi() {
        mainFragmentViewModel.reposPagedList.observe(requireActivity(), Observer {
            searchPagingAdapter.submitList(it)
        })

        mainFragmentViewModel.networkStatus.observe(requireActivity(), Observer {
            when (it.status) {
                NetworkStatus.NOT_FOUND -> {
                    it.message = getString(
                        R.string.error_not_found,
                        mainFragmentViewModel.query.value
                    )
                }
                NetworkStatus.ERROR -> {
                    if (mainFragmentViewModel.query.value.isNullOrEmpty()) {
                        it.message = getString(R.string.error_no_query)
                    } else {
                        it.message = getString(R.string.error_connection)
                    }
                }
            }
            binding.networkStatus = it
        })

        mainFragmentViewModel.authenticationState.observe(viewLifecycleOwner, Observer { it ->
            binding.searchBtn.isEnabled =
                it.equals(AuthenticationState.AUTHENTICATED)
        })

        /*mDisposable.add(
            RxViewObservable.fromTextView(binding.searchEdt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged() // prevent duplicate call with same query
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        mainFragmentViewModel.setQuery(it)
                    },
                    {
                        Log.e(TAG, it.message ?: "")
                    }
                )
        )*/
    }

    private fun getPersonalizationMessage(): String {
        return String.format(
            resources.getString(R.string.welcome_message) +
                    FirebaseAuth.getInstance().currentUser?.displayName,
        )
    }

    /** Github sign in option
     */
    private fun launchSignInFlow() {
        val auth = FirebaseAuth.getInstance()
        val provider = OAuthProvider.newBuilder("github.com")
        val result = auth.pendingAuthResult

        if (result == null) {
            auth.startActivityForSignInWithProvider(
                requireActivity(),
                provider.build()
            ).addOnSuccessListener {
                binding.searchBtn.isEnabled = true
            }
        }
    }

    private fun searchGitHubRepos() {
        mainFragmentViewModel.setQuery(binding.searchEdt.text.toString())
/*
       repository.searchGitHubRepo(binding.searchEdt.text.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result -> populateList(result) },
                { error ->
                    error.printStackTrace()
                })

        val disposable = CompositeDisposable()
        disposable.add(repository.searchGitHubRepo(binding.searchEdt.text.toString()).subscribeOn(Schedulers.io())
            .mergeWith(repository.searchGitHubRepo(binding.searchEdt.text.toString()).subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> populateList(result) },
                { error -> error.printStackTrace() }))*/

    }

    private fun openLastVisitedFragment() {
        findNavController().navigate(R.id.action_mainFragment_to_lastVisitedFragment)
    }

    override fun onRepoClick(repo: GitHubSearchItemModel) {
        val visitedFlag = "visited"

        try {
            val url = repo?.htmlUrl
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            repo?.visitedFlag = visitedFlag
//            binding.searchResultRv.adapter.
//            searchPagingAdapter.currentList.
            mainFragmentViewModel.saveDataIntoDb(repo.asDatabaseEntity())
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "No application can handle this request. Please install a web browser or check your URL.",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }


}