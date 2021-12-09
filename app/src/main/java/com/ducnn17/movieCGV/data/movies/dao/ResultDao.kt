package com.ducnn17.movieCGV.data.movies.dao

import androidx.room.*
import com.ducnn17.movieCGV.data.movies.entity.Result
@Dao
interface ResultDao {
    @Query("SELECT * FROM result")
     fun getAll(): List<Result>

    @Query("SELECT * FROM result WHERE id IN (:userIds)")
     fun loadAllByIds(userIds: IntArray): List<Result>

    @Query("SELECT * FROM result WHERE id IN (:id)")
     fun checkid (id: Int): Result


    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAll(vararg result: Result)

    @Delete
     fun delete(result: Result)
}