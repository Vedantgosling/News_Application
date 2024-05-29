package com.example.newsmd.data.RepoImpl

import com.example.newsmd.Util.Resource
import com.example.newsmd.data.remote.newsAPI
import com.example.newsmd.domain.Model.Article
import com.example.newsmd.domain.Repository.NewsRepository

class RepositoryImpl (
    private val newsapi: newsAPI
):NewsRepository
{ //Interface Implementation
    override suspend fun getTopHeadlines(category: String): Resource<List<Article>>//Returned Value
    {
        return try {
            val response = newsapi.getBreakingNews(category = category)
            Resource.Success(data = response.articles)//providing articles to resource class
        }catch(e:Exception){
            Resource.Error(message = "Failed to load data ${e.message}")

        }
    }


}