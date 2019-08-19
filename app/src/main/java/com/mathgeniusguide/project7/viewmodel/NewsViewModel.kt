package com.mathgeniusguide.project7.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mathgeniusguide.project7.api.ApiFactory
import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import kotlinx.coroutines.launch

class NewsViewModel(application: Application): AndroidViewModel(application) {

    val topNews: MutableLiveData<CategoryResponse?>? = MutableLiveData()
    val popularNews: MutableLiveData<PopularResponse?>? = MutableLiveData()
    val politicsNews: MutableLiveData<CategoryResponse?>? = MutableLiveData()

/*    init {
        topNews = MutableLiveData()
        popularNews = MutableLiveData()
        politicsNews = MutableLiveData()
    }*/

    fun fetchTopNews() {
        viewModelScope.launch {
            topNews?.postValue(ApiFactory.api.getTopStories().body())
        }
    }

    fun fetchPopularNews() {
        viewModelScope.launch {
            popularNews?.postValue(ApiFactory.api.getPopularNews().body())
        }
    }

    fun fetchPoliticsNews() {
        viewModelScope.launch {
            politicsNews?.postValue(ApiFactory.api.getPoliticsNews().body())
        }
    }

}