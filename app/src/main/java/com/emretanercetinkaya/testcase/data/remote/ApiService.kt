package com.emretanercetinkaya.testcase.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emretanercetinkaya.testcase.model.StationsResponseModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @GET("stations")
    fun getStations(): Call<List<StationsResponseModel>>

    @POST("stations/{station_id}/trips/{trip_id}")
    fun bookingTrip(
        @Path("station_id") stationId: Long,
        @Path("trip_id") tripId: Long
    ): Call<ResponseBody>

}