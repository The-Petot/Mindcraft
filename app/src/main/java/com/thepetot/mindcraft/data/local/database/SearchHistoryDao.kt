package com.thepetot.mindcraft.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(query: SearchHistoryEntity)

    @Update
    suspend fun update(query: SearchHistoryEntity)

    @Delete
    suspend fun delete(query: SearchHistoryEntity)

    @Query("SELECT * from search_history ORDER BY id DESC")
    fun getAll(): LiveData<List<SearchHistoryEntity>>
}