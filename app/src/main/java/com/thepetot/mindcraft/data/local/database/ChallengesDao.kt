package com.thepetot.mindcraft.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem

@Dao
interface ChallengesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(quote: List<DataItem>)

    @Query("SELECT * FROM challenges")
    fun getAllChallenges(): PagingSource<Int, DataItem>

    @Query("""
        SELECT * 
        FROM challenges
        WHERE 
            title LIKE '%' || :query || '%' OR
            description LIKE '%' || :query || '%' OR
            tags LIKE '%' || :query || '%'
        ORDER BY id DESC
    """)
    fun getChallengesByQuery(query: String): PagingSource<Int, DataItem>

    @Query("DELETE FROM challenges")
    suspend fun deleteAll()
}