package com.example.newsmd.Presentation.NewsScreen

import com.example.newsmd.domain.Model.Article


sealed class NewsScreenEvent {
    data class OnNewsCardClicked(var article: Article) : NewsScreenEvent()
    data class OnCategoryChanged(var category: String) : NewsScreenEvent()
    object OnCloseIconClicked: NewsScreenEvent()
}