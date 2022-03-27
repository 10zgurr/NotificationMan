package com.notificationman.library.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.notificationman.library.extensions.convertToList
import com.notificationman.library.extensions.convertToString
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.util.*
import kotlin.collections.ArrayList

class AppDataStoreImpl(
    private val dataStore: DataStore<Preferences>
): AppDataStore {

    companion object {
        private val WORKER_IDS = stringPreferencesKey("worker_ids_key")
    }

    override suspend fun saveWorkerId(id: UUID) {
        val ids = getWorkerIds() as ArrayList
        ids.add(id)
        saveIdsIntoDataStore(ids = ids)
    }

    override suspend fun deleteWorkerId(id: UUID) {
        val ids = getWorkerIds() as ArrayList
        ids.remove(id)
        saveIdsIntoDataStore(ids = ids)
    }

    override suspend fun deleteAllWorkerIds() {
        saveIdsIntoDataStore(ids = emptyList())
    }

    override suspend fun getWorkerIds(): List<UUID> {
        val ids = arrayListOf<UUID>()
        val json = dataStore
            .data
            .catch { e ->
                e.printStackTrace()
            }.first()[WORKER_IDS]
        val savedIds = json?.convertToList()
        if (!savedIds.isNullOrEmpty())
            ids.addAll(savedIds)
        return ids
    }

    override suspend fun getFirstWorkerId(): UUID? {
        return getWorkerIds().firstOrNull()
    }

    private suspend fun saveIdsIntoDataStore(ids: List<UUID>) {
        dataStore.edit { preferences ->
            val idsJsonString = ids.convertToString()
            if (!idsJsonString.isNullOrBlank())
                preferences[WORKER_IDS] = idsJsonString
        }
    }
}