package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.headwayTestTask.database.ReposDatabase
import com.example.headwayTestTask.model.GitHubSearchItemModel
import com.example.headwayTestTask.utils.asDomainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LastVisitedViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var dataBaseInstance: ReposDatabase?= null

    var lastVisitedReposList: MutableLiveData<List<GitHubSearchItemModel>> =
        MutableLiveData()

    fun setDatabaseInstance(dataBaseInstance: ReposDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun getPersonData(){
        dataBaseInstance?.repoDao?.getRepos()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if (!it.isNullOrEmpty()) {
                    lastVisitedReposList.postValue(it.asDomainModel())
                } else {
                    lastVisitedReposList.postValue(listOf())
                }
            },{
            })?.let {
                compositeDisposable.add(it)
            }
    }

}