package com.thepetot.mindcraft.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem

@Database(
    entities = [DataItem::class, RemoteKeys::class, SearchHistoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ChallengesDatabase : RoomDatabase() {
    abstract fun challengesDao(): ChallengesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: ChallengesDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ChallengesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChallengesDatabase::class.java, "challenges.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}