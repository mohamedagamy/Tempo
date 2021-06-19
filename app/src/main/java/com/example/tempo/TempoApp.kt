package com.example.tempo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

@HiltAndroidApp
class TempoApp : Application(){
    override fun onCreate() {
        super.onCreate()

        Timber.uprootAll()
        Timber.plant(Timber.DebugTree())
    }

    private fun initRxJavaError() {
        // If Java 8 lambdas are supported
        RxJavaPlugins.setErrorHandler { e: Throwable? ->
            Timber.d(e?.localizedMessage.toString())
        }

    }
}