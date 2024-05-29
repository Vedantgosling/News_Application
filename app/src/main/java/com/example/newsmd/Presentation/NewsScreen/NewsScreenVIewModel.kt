package com.example.newsmd.Presentation.NewsScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmd.Util.Resource
import com.example.newsmd.domain.Model.Article
import com.example.newsmd.domain.Repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenVIewModel @Inject constructor(
    private val newsRepository: NewsRepository//HILT forces it use newsRepo from Module and not from original
):ViewModel() {

    var articles by mutableStateOf<List<Article>>(emptyList())
    var state by mutableStateOf(NewsState())

    fun onEvent(event:NewsScreenEvent){
        when(event){
            is NewsScreenEvent.OnCategoryChanged -> {
                state = state.copy(category = event.category)
                getNewsArticles(category = state.category)
            }
            NewsScreenEvent.OnCloseIconClicked -> TODO()
            is NewsScreenEvent.OnNewsCardClicked -> {
                state = state.copy(selectedArticle = event.article)
            }
        }
    }

    init {
        getNewsArticles(category = "general")
    }

    private fun getNewsArticles(category: String){
        viewModelScope.launch {
            val result = newsRepository.getTopHeadlines(category = category)
            when(result){
                is Resource.Success -> {
                    state = state.copy(
                        articles =result.data?: emptyList()
                    )
                }
                is Resource.Error -> TODO()
            }
        }
    }
}
