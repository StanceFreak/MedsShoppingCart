package com.example.testing.di

import com.example.testing.data.api.repository.AppRepository
import com.example.testing.data.api.repository.LocalDataSource
import com.example.testing.data.api.repository.RemoteDataSource
import org.koin.dsl.module

//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//    @Singleton
//    @Provides
//}
val repoModule = module {
    single {
        AppRepository(get(), get())
    }
    single {
        RemoteDataSource(get())
    }
    single {
        LocalDataSource(get())
    }
}