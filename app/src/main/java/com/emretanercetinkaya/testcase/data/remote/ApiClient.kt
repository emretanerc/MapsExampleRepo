package com.emretanercetinkaya.testcase.data.remote

import com.emretanercetinkaya.testcase.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Singleton
fun ApiClient(): ApiService {
    val client = OkHttpClient.Builder()
        .build()
    val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()
    return retrofitBuilder.create(ApiService::class.java)
}
