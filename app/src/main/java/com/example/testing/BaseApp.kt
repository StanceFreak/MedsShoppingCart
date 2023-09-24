package com.example.testing

import android.app.Application
import com.example.testing.di.networkModule
import com.example.testing.di.preferenceModule
import com.example.testing.di.repoModule
import com.example.testing.di.viewModelModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

//@HiltAndroidApp
class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@BaseApp)
            androidLogger(Level.ERROR)
            modules(networkModule, viewModelModule, repoModule, preferenceModule)
        }
    }

}