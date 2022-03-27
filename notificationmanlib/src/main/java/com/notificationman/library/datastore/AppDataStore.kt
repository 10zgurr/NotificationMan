package com.notificationman.library.datastore

import java.util.*

interface AppDataStore {

    suspend fun saveWorkerId(id: UUID)

    suspend fun deleteWorkerId(id: UUID)

    suspend fun deleteAllWorkerIds()

    suspend fun getWorkerIds(): List<UUID>

    suspend fun getFirstWorkerId(): UUID?
}