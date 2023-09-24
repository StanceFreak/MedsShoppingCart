package com.example.testing.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    fun getLoginState(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[LOGIN_STATE] ?: false
        }
    }

    fun getUserData(): Flow<String> {
        return context.dataStore.data.map {
            it[USER_DATA] ?: ""
        }
    }

    suspend fun storeLoginState(state: Boolean) {
        context.dataStore.edit {
            it[LOGIN_STATE] = state
        }
    }

    suspend fun storeUserData(uid: String) {
        context.dataStore.edit {
            it[USER_DATA] = uid
        }
    }

    suspend fun removeSession() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
        val LOGIN_STATE = booleanPreferencesKey("LOGIN_STATE")
        val USER_DATA = stringPreferencesKey("USER_DATA")
    }

}