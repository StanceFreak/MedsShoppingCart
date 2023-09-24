package com.example.testing.di

import android.content.Context
import com.example.testing.data.local.DataStoreManager
import org.koin.dsl.module

private fun provideDataStore(context: Context): DataStoreManager = DataStoreManager(context)

val preferenceModule = module {
    single { provideDataStore(get()) }
}