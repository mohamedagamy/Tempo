package com.example.tempo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.tempo.data.model.Article
import com.example.tempo.data.model.ApiResult
import com.example.tempo.data.model.Resource
import com.example.tempo.data.repo.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel  @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    val newsList:MutableLiveData<List<Article>> = MutableLiveData()

    fun getNewsList(query:String, pageNumber: Int) = liveData(Dispatchers.IO) {
        viewModelScope.launch {
            newsList.postValue(appRepository.getAllNews(query,pageNumber).articles)
        }
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = null))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun searchNews(searchKey: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.searchNewsList()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}
