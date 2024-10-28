package com.soho.sohoapp.live.db

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromAgentProperty(agentProperty: AgentProperty?): String? {
        return agentProperty?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toAgentProperty(agentPropertyString: String?): AgentProperty? {
        return agentPropertyString?.let { Json.decodeFromString(it) }
    }
}
