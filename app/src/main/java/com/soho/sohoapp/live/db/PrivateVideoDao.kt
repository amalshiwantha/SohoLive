package com.soho.sohoapp.live.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrivateVideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: PrivateVideo)

    @Query("SELECT * FROM private_videos WHERE filePath = :filePath")
    suspend fun getVideoByPath(filePath: String): PrivateVideo?

    @Query("SELECT * FROM private_videos")
    suspend fun getAllVideos(): List<PrivateVideo>
}
