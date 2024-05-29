package com.example.newsmd.Presentation.NewsScreen

import com.example.newsmd.domain.Model.Article

data class NewsState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null,
    val selectedArticle: Article? = null,
    val category: String = "General",

)