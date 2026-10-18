package com.mysynthetic.agent.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mysynthetic.agent.data.model.MessageEntity
import com.mysynthetic.agent.data.model.SessionEntity

@Database(
    entities = [MessageEntity::class, SessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun sessionDao(): SessionDao

    companion object {
        const val DATABASE_NAME = "mysynthetic_agent_db"
    }
}
