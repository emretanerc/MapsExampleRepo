package com.emretanercetinkaya.testcase.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emretanercetinkaya.testcase.model.StationsResponseModel
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("stations")
    fun getStations(): Call<List<StationsResponseModel>>
}