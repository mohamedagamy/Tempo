package com.example.tempo.data.repo

import com.example.tempo.data.api.ApiService
import com.example.tempo.data.model.ApiResult
import io.reactivex.Single
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllNews(query:String, page:Int = 1) = apiService.getNewsList(query,page)

    suspend fun searchNewsList() = apiService.searchNewsList("")

}
