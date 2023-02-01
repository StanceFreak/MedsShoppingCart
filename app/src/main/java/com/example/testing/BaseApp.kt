package com.example.testing

import android.app.Application
import com.example.testing.di.networkModule
import com.example.testing.di.repoModule
import com.example.testing.di.viewModelModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.core.context.startKoin

//@HiltAndroidApp
class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(networkModule, viewModelModule, repoModule)
        }
    }

}