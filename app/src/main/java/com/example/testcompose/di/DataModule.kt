package com.example.testcompose.di

import com.example.testcompose.data.repository.MovieRepository
import com.example.testcompose.data.repository.MovieRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun provideRepository(repositoryImp: MovieRepositoryImp): MovieRepository

}