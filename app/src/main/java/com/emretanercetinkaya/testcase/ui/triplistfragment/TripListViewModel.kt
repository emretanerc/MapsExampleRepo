package com.emretanercetinkaya.testcase.ui.triplistfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretanercetinkaya.testcase.data.remote.ApiClient
import com.emretanercetinkaya.testcase.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TripListViewModel : ViewModel() {

    fun tripBooking(stationId: Long, tripId: Long, callback: (Boolean) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            ApiClient().bookingTrip(stationId, tripId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        callback(true)
                    } else {
                        if (response.code() == 400) {
                            callback(false)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(false)
                    t.printStackTrace()
                    t.toString()
                }
            })
        }
    }
}