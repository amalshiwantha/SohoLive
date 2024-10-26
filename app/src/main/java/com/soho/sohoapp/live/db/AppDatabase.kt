package com.soho.sohoapp.live.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PrivateVideo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun privateVideoDao(): PrivateVideoDao
}
