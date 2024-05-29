package com.example.newsmd.DI

import com.example.newsmd.data.RepoImpl.RepositoryImpl
import com.example.newsmd.data.remote.newsAPI
import com.example.newsmd.data.remote.newsAPI.Companion.BASE_URL
import com.example.newsmd.domain.Repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideNewsApi():newsAPI{// this is a return function newsAPI gets the returning retrofit info
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(newsAPI::class.java)//this block data
    }

    @Provides
    @Singleton
    fun providesNewsRepository(newsaPI: newsAPI):NewsRepository{ // newsaPI then uses the above newsAPI function
        return RepositoryImpl(newsaPI)
    }

}