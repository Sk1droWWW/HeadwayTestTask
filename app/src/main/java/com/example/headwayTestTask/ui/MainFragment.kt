package com.example.headwayTestTask.ui

import android.app.Activity
import android.content.Intent
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
import com.example.headwayTestTask.R
import com.example.headwayTestTask.databinding.MainFragmentBinding
import com.example.headwayTestTask.viewmodels.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider


class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001

        fun newInstance() = MainFragment()
    }

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()

    //    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: MainFragmentBinding

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

        binding.loginButton.setOnClickListener { launchSignInFlow() }
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

    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
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
                Toast.makeText(requireContext(), "yep", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "nope", Toast.LENGTH_LONG).show()
            }
        } else {
            result.addOnSuccessListener {
                Toast.makeText(requireContext(), "yep", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "nope", Toast.LENGTH_LONG).show()
            }
        }

        /* val providers = arrayListOf(
 //            AuthUI.IdpConfig.EmailBuilder().build(),
             AuthUI.IdpConfig.GitHubBuilder().build()
 //            OAuthProvider.newBuilder("github.com").build()
             //
         )*/

        /* startActivityForResult(
             AuthUI.getInstance()
                 .createSignInIntentBuilder()
                 .setAvailableProviders(providers)
                 .setIsSmartLockEnabled(false)
                 .build(),
             MainFragment.SIGN_IN_RESULT_CODE
         )*/
    }
}