package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.headwayTestTask.network.model.GitHubSearchItemModel

class MainViewModel : ViewModel() {

    var itemList: MutableList<GitHubSearchItemModel> = mutableListOf()

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    // TODO migrate to RxJava
    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun onRepoClicked() {

    }


    fun makeApiCall(query: String) {

    }

}


