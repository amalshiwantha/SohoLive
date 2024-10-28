package com.soho.sohoapp.live.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    // Convert AgentProperty to JSON
    @TypeConverter
    fun fromAgentProperty(agentProperty: AgentProperty?): String? {
        return agentProperty?.let { json.encodeToString(it) }
    }

    // Convert JSON to AgentProperty
    @TypeConverter
    fun toAgentProperty(agentPropertyString: String?): AgentProperty? {
        return agentPropertyString?.let { json.decodeFromString(it) }
    }
}
