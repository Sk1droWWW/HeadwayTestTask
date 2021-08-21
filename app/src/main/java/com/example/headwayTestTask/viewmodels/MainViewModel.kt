package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.headwayTestTask.database.DatabaseRepos
import com.example.headwayTestTask.database.ReposDatabase
import com.example.headwayTestTask.network.NetworkStatus
import com.example.headwayTestTask.model.datasource.PagingDataSourceFactory
import com.example.headwayTestTask.model.datasource.PagingListener
import com.example.headwayTestTask.model.GitHubSearchItemModel
import com.example.headwayTestTask.repository.SearchRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

//TODO - Migrate to PagingSource and PagingData
class MainViewModel(private val searchRepository: SearchRepository) : ViewModel(),
    PagingListener<GitHubSearchItemModel> {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    private val compositeDisposable = CompositeDisposable()
    private var dataBaseInstance: ReposDatabase?= null
    fun setDatabaseInstance(dataBaseInstance: ReposDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun saveDataIntoDb(repo: DatabaseRepos){

        dataBaseInstance?.repoDao?.insertRepo(repo)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }

        dataBaseInstance?.repoDao?.del()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }

    }

    fun getPersonData(){

        dataBaseInstance?.repoDao?.getRepos()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if (!it.isNullOrEmpty()) {
//                    personsList.postValue(it)
                } else {
//                    personsList.postValue(listOf())
                }
            },{
            })?.let {
                compositeDisposable.add(it)
            }
    }



    private val PAGE_SIZE = 30

    // TODO migrate to RxJava
    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }


    val query: MutableLiveData<String> = MutableLiveData()
    val userPagedList: LiveData<PagedList<GitHubSearchItemModel>> =
        Transformations.switchMap(Transformations.distinctUntilChanged(query)) {
            search()
        }

    private var mNetworkStatus: MutableLiveData<NetworkStatus> = MutableLiveData()
    val networkStatus: LiveData<NetworkStatus>
        get() = mNetworkStatus

    private val mDisposable = CompositeDisposable()
    private val mPagingDataSourceFactory: PagingDataSourceFactory<GitHubSearchItemModel> =
        PagingDataSourceFactory(this)


    fun setQuery(value: String) {
        query.postValue(value)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()

        super.onCleared()
        mDisposable.clear()
    }

    private fun search(): LiveData<PagedList<GitHubSearchItemModel>> {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .build()

        return LivePagedListBuilder<Int, GitHubSearchItemModel>(
            mPagingDataSourceFactory,
            config
        ).build()
    }

    override fun loadInitial(
        loadInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, GitHubSearchItemModel>
    ) {
        if (query.value.isNullOrEmpty()){
            mNetworkStatus.postValue(NetworkStatus(NetworkStatus.ERROR, true))
            return
        }

        mNetworkStatus.postValue(NetworkStatus(NetworkStatus.LOADING))
        val queryParam = query.value ?: ""
        mDisposable.add(
            searchRepository.search(queryParam, 1)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.isEmpty()) {
                            mNetworkStatus
                                .postValue(NetworkStatus(NetworkStatus.NOT_FOUND, true))
                        } else {
                            mNetworkStatus
                                .postValue(NetworkStatus(NetworkStatus.SUCCESS))
                            loadInitialCallback.onResult(it, null, 2)
                        }

                    },
                    {
                        mNetworkStatus
                            .postValue(NetworkStatus(NetworkStatus.ERROR, true))
                    }
                )
        )
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, GitHubSearchItemModel>
    ) {
        val queryParam = query.value ?: ""
        mDisposable.add(
            searchRepository.search(queryParam, params.key)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    callback.onResult(it, params.key + 1)
                }
        )
    }


}


