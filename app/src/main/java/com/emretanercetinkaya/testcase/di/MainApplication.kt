package com.emretanercetinkaya.testcase.di

import android.content.Context
import dagger.android.DaggerApplication


class MainApplication : DaggerApplication() {
    private val applicationInjector = DaggerAppComponent.builder().application(this)?.build()

    private var instance: MainApplication? = null

    fun getInstance(): MainApplication? {
        return instance
    }

    fun getContext(): Context? {
        return instance
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    override fun applicationInjector() = applicationInjector
}
