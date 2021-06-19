package com.example.tempo.data.api
import com.example.tempo.data.model.ApiResult
import retrofit2.http.*

interface ApiService {
    @GET("everything?pageSize=20&apiKey=cdb1446ae5014a74a16f0b1573f19517")
    suspend fun getNewsList(@Query("q") searchKey: String,@Query("page") page: Int): ApiResult

    @GET("everything?q=bitcoin&apiKey=8cfa9fcf90924f42be7b344c917e8da1")
    suspend fun searchNewsList(@Query("") searchKey: String): ApiResult

}