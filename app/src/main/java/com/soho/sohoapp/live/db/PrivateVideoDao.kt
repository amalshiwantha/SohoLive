package com.soho.sohoapp.live.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PrivateVideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: PrivateVideo): Long

    @Query("SELECT * FROM private_videos WHERE id = :id")
    suspend fun getVideoById(id: Long): PrivateVideo?

    @Query("SELECT * FROM private_videos WHERE filePath = :filePath")
    suspend fun getVideoByPath(filePath: String): PrivateVideo?

    @Query("SELECT * FROM private_videos")
    suspend fun getAllVideos(): List<PrivateVideo>

    @Query("SELECT * FROM private_videos ORDER BY createdDate DESC LIMIT 1")
    suspend fun getLatestVideo(): PrivateVideo?

    @Update
    suspend fun updateVideo(video: PrivateVideo)
}
