package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LastVisitedViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LastVisitedViewModel::class.java)) {
            return LastVisitedViewModel() as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}