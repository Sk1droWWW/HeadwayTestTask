package com.example.headwayTestTask.viewmodels

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.headwayTestTask.database.DatabaseRepos
import com.example.headwayTestTask.database.ReposDatabase
import com.example.headwayTestTask.model.GitHubSearchItemModel
import com.example.headwayTestTask.model.datasource.PagingDataSourceFactory
import com.example.headwayTestTask.model.datasource.PagingListener
import com.example.headwayTestTask.network.NetworkStatus
import com.example.headwayTestTask.repository.SearchRepository
import com.example.headwayTestTask.utils.AuthenticationState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val PAGE_SIZE = 15

class MainViewModel(private val searchRepository: SearchRepository) : ViewModel(),
    PagingListener<GitHubSearchItemModel> {

    private val compositeDisposable = CompositeDisposable()
    private val mPagingDataSourceFactory: PagingDataSourceFactory<GitHubSearchItemModel> =
        PagingDataSourceFactory(this)
    val query: MutableLiveData<String> = MutableLiveData()
    val token: MutableLiveData<String> = MutableLiveData()

    private var dataBaseInstance: ReposDatabase? = null

    val reposPagedList: LiveData<PagedList<GitHubSearchItemModel>> =
        Transformations.switchMap(Transformations.distinctUntilChanged(query)) {
            search()
        }

    private var mNetworkStatus: MutableLiveData<NetworkStatus> = MutableLiveData()
    val networkStatus: LiveData<NetworkStatus>
        get() = mNetworkStatus

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    /**
     * Set Repos Database instance
     */
    fun setDatabaseInstance(dataBaseInstance: ReposDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    /**
     * Add row to DatabaseRepos entity of Repos database, after that delete old rows
     *
     * @param repo DatabaseRepos object, which we want to add
     */
    fun saveDataIntoDb(repo: DatabaseRepos) {
        insertRepo(repo)
        limitDbSize()
    }

    /**
     * Drops old items
     */
     private fun limitDbSize() {
        dataBaseInstance?.repoDao?.deleteOldRepos()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    /**
     * Add row to DatabaseRepos entity of Repos database and
     *
     * @param repo DatabaseRepos object, which we add
     */
    private fun insertRepo(repo: DatabaseRepos) {
        dataBaseInstance?.repoDao?.insertRepo(repo)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(Schedulers.io())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    /**
     * Set LivaData value which represent search input string
     *
     * @param value search input
     */
    fun setQuery(value: String) {
        query.postValue(value)
    }

    fun setToken(value: String?) {
        token.postValue(value ?: "")
    }

    /**
     * Init PagedList config
     *
     * @return LiveData list
     */
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

    /**
     * Load initial search response and set callback for PagedList
     */
    override fun loadInitial(
        loadInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, GitHubSearchItemModel>
    ) {
        if (query.value.isNullOrEmpty()) {
            mNetworkStatus.postValue(NetworkStatus(NetworkStatus.ERROR, true))
            return
        }

        mNetworkStatus.postValue(NetworkStatus(NetworkStatus.LOADING))
        val queryParam = query.value ?: ""
        val tokenParam = token.value ?: ""

        val firstQuery = searchRepository.search(tokenParam, queryParam, 1)
            .subscribeOn(Schedulers.io())
        val secondQuery = searchRepository.search(tokenParam, queryParam, 2)
            .subscribeOn(Schedulers.io())

        compositeDisposable.add(
            Observable.zip(firstQuery, secondQuery, { t1, t2 -> t1 + t2 })
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

    /**
     * Uploads following after initial search response and set callback for PagedList
     */
    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, GitHubSearchItemModel>
    ) {
        val queryParam = query.value ?: ""
        val tokenParam = token.value ?: ""

        val firstQuery =
            searchRepository.search(tokenParam, queryParam, params.key)
                .subscribeOn(Schedulers.io())
        val secondQuery =
            searchRepository.search(tokenParam, queryParam, params.key + 1)
                .subscribeOn(Schedulers.io())

        compositeDisposable.add(
            Observable.zip(firstQuery, secondQuery, { t1, t2 -> t1 + t2 })
                .subscribe (
                    {
                        callback.onResult(it, params.key + 2)
                    }, {
                        mNetworkStatus
                            .postValue(NetworkStatus(NetworkStatus.ERROR, true))
                    }
                )
        )
    }

    /**
     * Clear disposable
     */
    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

}


