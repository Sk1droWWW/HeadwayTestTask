package com.example.headwayTestTask.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource


class PagingDataSourceFactory<Model : Any>(
    private val pagingListener: PagingListener<Model>
) : DataSource.Factory<Int, Model>() {

    val pagingDataSource = MutableLiveData<PagingDataSource<Model>>()

    override fun create(): DataSource<Int, Model> {
        val dataSource = PagingDataSource(pagingListener)
        pagingDataSource.postValue(dataSource)

        return dataSource
    }
}