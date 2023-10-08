package com.emretanercetinkaya.testcase.ui.stationsmapfragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emretanercetinkaya.testcase.data.remote.ApiClient
import com.emretanercetinkaya.testcase.model.CustomMarkerData
import com.emretanercetinkaya.testcase.model.StationsResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StationsMapViewModel(application: Application) : AndroidViewModel(application) {

    private val localMarkerList : ArrayList<CustomMarkerData> = ArrayList()
    private val _markersLiveData = MutableLiveData<ArrayList<CustomMarkerData>>()
    val markerList: LiveData<ArrayList<CustomMarkerData>>
        get() = _markersLiveData

    init {
        getStationsFromRemote()
    }

    private fun getStationsFromRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            ApiClient().getStations().enqueue(object : Callback<List<StationsResponseModel>> {
                override fun onResponse(call: Call<List<StationsResponseModel>>, response: Response<List<StationsResponseModel>>) {
                    if (response.isSuccessful) {
                        for (item in response.body()!!) {
                            localMarkerList.add(CustomMarkerData(item.trips_count.toString() + " Trips",item.getLatLng(),item.id,item.trips,false))
                        }
                        _markersLiveData.postValue(localMarkerList);
                    }
                }

                override fun onFailure(call: Call<List<StationsResponseModel>>, t: Throwable) {
                    Log.d("Hata", t.toString())
                }
            })
        }
    }

    fun changeTripState(stationId: Long) {
        for (item in localMarkerList) {
            if (item.stationId == stationId) {
                item.isBooked = true
                _markersLiveData.postValue(localMarkerList)
                break;
            }
        }
    }

}
