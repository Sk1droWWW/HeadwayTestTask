package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.headwayTestTask.network.model.GitHubSearchItemModel
import io.reactivex.Observable

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

//    var searchButtonState = LiveData<Boolean>(authenticationState == AuthenticationState.AUTHENTICATED)
//
//    private val searchButtonStateMutable = MutableLiveData<Boolean>()
//    val searchButtonState = searchButtonStateMutable as LiveData<Boolean>

    fun onRepoClicked() {

    }


    fun makeApiCall(query: String) {

    }

}


