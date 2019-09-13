package com.mathgeniusguide.project7.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mathgeniusguide.project7.api.ApiFactory
import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import com.mathgeniusguide.project7.responses.search.SearchResponseFull
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    // declare MutableLiveData variables for use in this class
    val _topNews: MutableLiveData<CategoryResponse?>? = MutableLiveData()
    val _popularNews: MutableLiveData<PopularResponse?>? = MutableLiveData()
    val _politicsNews: MutableLiveData<CategoryResponse?>? = MutableLiveData()
    val _searchNews: MutableLiveData<SearchResponseFull>? = MutableLiveData()

    // declare LiveData variables for observing in other classes
    val topNews: LiveData<CategoryResponse?>?
        get() = _topNews
    val popularNews: LiveData<PopularResponse?>?
        get() = _popularNews
    val politicsNews: LiveData<CategoryResponse?>?
        get() = _politicsNews
    val searchNews: LiveData<SearchResponseFull>?
        get() = _searchNews

    // fetch news
    fun fetchTopNews() {
        viewModelScope.launch {
            _topNews?.postValue(ApiFactory.api.getTopStories().body())
        }
    }
    fun fetchPopularNews() {
        viewModelScope.launch {
            _popularNews?.postValue(ApiFactory.api.getPopularNews().body())
        }
    }
    fun fetchPoliticsNews() {
        viewModelScope.launch {
            _politicsNews?.postValue(ApiFactory.api.getPoliticsNews().body())
        }
    }
    fun fetchSearchNews(query: String, categories: String, beginDate: String, endDate: String) {
        viewModelScope.launch {
            _searchNews?.postValue(ApiFactory.api.getSearchNews(query, categories, beginDate, endDate).body())
        }
    }
}