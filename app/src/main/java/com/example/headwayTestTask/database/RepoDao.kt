package com.example.headwayTestTask.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface RepoDao {
    @Query("SELECT * FROM ${DatabaseRepos.TABLE_NAME}")
    fun getRepos(): Flowable<List<DatabaseRepos>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepo(repo: DatabaseRepos) : Completable

    @Delete
    fun deleteRepo(repo: DatabaseRepos) : Completable

    @Query("DELETE FROM ${DatabaseRepos.TABLE_NAME} " +
            "where id NOT IN " +
            "(SELECT id from ${DatabaseRepos.TABLE_NAME} " +
            "ORDER BY id DESC LIMIT ${DatabaseRepos.MAX_SIZE})")
    fun del() : Completable
}