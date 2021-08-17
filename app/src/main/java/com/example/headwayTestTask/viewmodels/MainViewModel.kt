package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.headwayTestTask.network.model.GitHubSearchItemModel

class MainViewModel : ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    lateinit var reposList: MutableLiveData<GitHubSearchItemModel>


    init {
        reposList = MutableLiveData()
    }

    fun onRepoClicked() {

    }

    fun getReposListObserver(): MutableLiveData<GitHubSearchItemModel> {
        return reposList
    }

    fun makeApiCall(query: String) {

    }

}