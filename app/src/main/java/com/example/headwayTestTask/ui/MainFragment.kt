package com.example.headwayTestTask.ui

import android.app.Activity
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headwayTestTask.R
import com.example.headwayTestTask.databinding.MainFragmentBinding
import com.example.headwayTestTask.network.model.GitHubSearchItemModel
import com.example.headwayTestTask.network.model.GitHubSearchModel
import com.example.headwayTestTask.network.service.GithubApiService
import com.example.headwayTestTask.network.service.SearchRepositoryProvider
import com.example.headwayTestTask.ui.adapter.GitHubSearchAdapter
import com.example.headwayTestTask.viewmodels.MainViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.content.ActivityNotFoundException
import androidx.core.content.ContextCompat


class MainFragment : Fragment(), GitHubSearchAdapter.GitHubSearchItemClickListener {

    companion object {
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001

        fun newInstance() = MainFragment()
    }

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<MainViewModel>()

    //    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    // Adapter
    private val searchAdapter: GitHubSearchAdapter
            by lazy { GitHubSearchAdapter(this) }


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
        observeAuthenticationState()

        // Get a reference to the ViewModel associated with this fragment.
        val mainFragmentViewModel = MainViewModel()

        binding.searchResult.layoutManager = LinearLayoutManager(requireContext())
        binding.searchResult.adapter = searchAdapter
        binding.loginButton.setOnClickListener { launchSignInFlow() }

        binding.searchButton.setOnClickListener {
            val apiService = GithubApiService.create()
            val repository = SearchRepositoryProvider.provideSearchRepository(apiService)
            repository.searchGitHubRepo(binding.searchEditText.text.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                        result ->
                    populateList(result)
                }, { error ->
                    error.printStackTrace()
                })
        }

        mainFragmentViewModel.authenticationState.observe(viewLifecycleOwner, Observer { it ->
            binding.searchButton.isEnabled =
                it.equals(MainViewModel.AuthenticationState.AUTHENTICATED)
        })
    }

    private fun populateList(response: GitHubSearchModel?) {
        val searchItemList = response?.items
        searchAdapter.clearAll()
        searchAdapter.addData(searchItemList)
    }

   /* private fun setRecyclerData(it: GitHubSearchModel?) {
        binding.searchResult.adapter = GitHubSearchAdapter()

    }*/

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

    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                MainViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.welcomeText.text = getPersonalizationMessage()

                    binding.loginButton.text = getString(R.string.logout_button_text)
                    binding.loginButton.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }
                }
                else -> {
                    binding.welcomeText.text = resources.getString(R.string.welcome_message)

                    binding.loginButton.text = getString(R.string.login_button_text)
                    binding.loginButton.setOnClickListener {
                        launchSignInFlow()
                    }
                }
            }
        })
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
                binding.searchButton.isEnabled = true
            }
        } else {
            result.addOnSuccessListener {
                Toast.makeText(requireContext(), "yep", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "nope", Toast.LENGTH_LONG).show()
            }
        }
    }

     override fun onGitHubSearchItemClicked(item: GitHubSearchItemModel) {
         try {
             val url = item.htmlUrl
             val intent = Intent(Intent.ACTION_VIEW)
             intent.data = Uri.parse(url)
             startActivity(intent)
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