package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.headwayTestTask.repository.SearchRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val searchRepository: SearchRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(searchRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}