package com.emretanercetinkaya.testcase.ui.stationsmapfragment

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emretanercetinkaya.testcase.data.remote.ApiClient
import com.emretanercetinkaya.testcase.model.StationsResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StationsMapViewModel(application: Application) : AndroidViewModel(application) {

    private val _stationsLiveData = MutableLiveData<List<StationsResponseModel>>()
    val stationsLiveData: LiveData<List<StationsResponseModel>>
        get() = _stationsLiveData

    init {
        getStationsFromRemote()
    }

    private fun getStationsFromRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            ApiClient().getStations().enqueue(object : Callback<List<StationsResponseModel>> {
                override fun onResponse(call: Call<List<StationsResponseModel>>, response: Response<List<StationsResponseModel>>) {
                    if (response.isSuccessful) {
                        _stationsLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<StationsResponseModel>>, t: Throwable) {
                    Log.d("Hata", t.toString())
                }
            })
        }
    }
}
