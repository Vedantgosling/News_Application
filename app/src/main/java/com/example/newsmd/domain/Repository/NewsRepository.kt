package com.example.newsmd.domain.Repository

import com.example.newsmd.Util.Resource
import com.example.newsmd.domain.Model.Article

interface NewsRepository {
    suspend fun getTopHeadlines(
        category: String
    ):Resource<List<Article>> //resource class will hold the list of news articles
}