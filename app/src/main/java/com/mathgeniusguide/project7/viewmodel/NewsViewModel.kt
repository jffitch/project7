package com.mathgeniusguide.project7.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mathgeniusguide.project7.api.Api
import com.mathgeniusguide.project7.connectivity.ConnectivityInterceptor
import com.mathgeniusguide.project7.connectivity.NoConnectivityException
import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import com.mathgeniusguide.project7.responses.search.SearchResponseFull
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    // declare MutableLiveData variables for use in this class
    // private as MutableLiveData should not be accessible from outside
    private val _topNews: MutableLiveData<CategoryResponse?>? = MutableLiveData()
    private val _popularNews: MutableLiveData<PopularResponse?>? = MutableLiveData()
    private val _politicsNews: MutableLiveData<CategoryResponse?>? = MutableLiveData()
    private val _searchNews: MutableLiveData<SearchResponseFull>? = MutableLiveData()
    private val _dataLoading = MutableLiveData<Boolean>()
    private val _isDataLoadingError = MutableLiveData<Boolean>()
    // for testing purposes
    val testNews: MutableLiveData<CategoryResponse?>? = MutableLiveData()

    // declare LiveData variables for observing in other classes
    val topNews: LiveData<CategoryResponse?>?
        get() = _topNews
    val popularNews: LiveData<PopularResponse?>?
        get() = _popularNews
    val politicsNews: LiveData<CategoryResponse?>?
        get() = _politicsNews
    val searchNews: LiveData<SearchResponseFull>?
        get() = _searchNews
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading
    val isDataLoadingError: LiveData<Boolean>
        get() = _isDataLoadingError

    // fetch news_fragment
    fun fetchTopNews() {
        val connectivityInterceptor = ConnectivityInterceptor(getApplication())
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                _topNews?.postValue(Api.invoke(connectivityInterceptor).getTopStories().body())
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(false)
            } catch (e: NoConnectivityException) {
                _isDataLoadingError.postValue(true)
                _dataLoading.postValue(false)
            }
        }
    }

    fun fetchPopularNews() {
        val connectivityInterceptor = ConnectivityInterceptor(getApplication())
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                _popularNews?.postValue(Api.invoke(connectivityInterceptor).getPopularNews().body())
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(false)
            } catch (e: NoConnectivityException) {
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(true)
            }
        }
    }

    fun fetchPoliticsNews() {
        val connectivityInterceptor = ConnectivityInterceptor(getApplication())
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                _politicsNews?.postValue(Api.invoke(connectivityInterceptor).getPoliticsNews().body())
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(false)
            } catch (e: NoConnectivityException) {
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(true)
            }
        }
    }

    fun fetchSearchNews(query: String, categories: String, beginDate: String, endDate: String) {
        val connectivityInterceptor = ConnectivityInterceptor(getApplication())
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                _searchNews?.postValue(
                    Api.invoke(connectivityInterceptor).getSearchNews(
                        query,
                        categories,
                        beginDate,
                        endDate
                    ).body()
                )
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(false)

            } catch (e: NoConnectivityException) {
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(true)
            }
        }
    }

    fun fetchTestNews() {
        val connectivityInterceptor = ConnectivityInterceptor(getApplication())
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                testNews?.postValue(Api.invoke(connectivityInterceptor).getTopStories().body())
                _dataLoading.postValue(false)
                _isDataLoadingError.postValue(false)
            } catch (e: NoConnectivityException) {
                _isDataLoadingError.postValue(true)
                _dataLoading.postValue(false)
            }
        }
    }
}