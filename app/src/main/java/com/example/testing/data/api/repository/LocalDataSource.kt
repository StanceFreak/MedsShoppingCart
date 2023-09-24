package com.example.testing.data.api.repository

import com.example.testing.data.local.DataStoreManager
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val manager: DataStoreManager
) {
    fun getLoginState(): Flow<Boolean> {
        return manager.getLoginState()
    }
    fun getUserData(): Flow<String> {
        return manager.getUserData()
    }

    suspend fun storeLoginState(state: Boolean) {
        return manager.storeLoginState(state)
    }
    suspend fun storeUserData(uid: String) {
        return manager.storeUserData(uid)
    }

    suspend fun removeSession() {
        return manager.removeSession()
    }
}