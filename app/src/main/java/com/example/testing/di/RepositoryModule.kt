package com.example.testing.di

import com.example.testing.data.api.repository.MedicineRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        MedicineRepository(get())
    }
}