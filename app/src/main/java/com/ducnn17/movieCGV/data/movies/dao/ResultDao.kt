package com.ducnn17.movieCGV.data.movies.dao

import androidx.room.*
import com.ducnn17.movieCGV.data.movies.entity.Result
@Dao
interface ResultDao {
    @Query("SELECT * FROM result")
    suspend fun getAll(): List<Result>

    @Query("SELECT * FROM result WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Result>

    @Query("SELECT * FROM result WHERE id IN (:id)")
    suspend fun checkid (id: Int): Result


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg result: Result)

    @Delete
    suspend fun delete(result: Result)
}