package com.soho.sohoapp.live.datastore

interface AppDataStore {
    suspend fun setValue(key: String, value: String)
    suspend fun readValue(key: String): String?
}