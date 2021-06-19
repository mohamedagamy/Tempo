package com.example.tempo

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        Timber.d("ErrorInterceptor : ${response.code}")
        return response
    }
}