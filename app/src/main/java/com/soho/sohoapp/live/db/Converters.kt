package com.soho.sohoapp.live.db

import androidx.room.TypeConverter
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.network.response.Document
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromDocument(document: Document?): String? {
        return document?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toDocument(data: String?): Document? {
        return data?.let { Json.decodeFromString<Document>(it) }
    }

    @TypeConverter
    fun fromVideoPrivacy(value: VideoPrivacy): String = value.name

    @TypeConverter
    fun toVideoPrivacy(value: String): VideoPrivacy = VideoPrivacy.valueOf(value)
}
