package com.notificationman.library.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val NOTIFICATION_MAN_PREFERENCES = "notification_man_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NOTIFICATION_MAN_PREFERENCES)